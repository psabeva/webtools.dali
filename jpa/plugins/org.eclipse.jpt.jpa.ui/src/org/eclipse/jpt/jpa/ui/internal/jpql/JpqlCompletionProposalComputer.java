/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.jpql;

import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.BASIC;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.ELEMENT_COLLECTION;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.EMBEDDED;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.EMBEDDED_ID;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.ID;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.MANY_TO_MANY;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.MANY_TO_ONE;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.ONE_TO_MANY;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.ONE_TO_ONE;
import static org.eclipse.persistence.jpa.jpql.spi.IMappingType.VERSION;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.core.internal.utility.PlatformTools;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.jpa.core.JpaPreferences;
import org.eclipse.jpt.jpa.core.context.NamedQuery;
import org.eclipse.jpt.jpa.core.jpql.JpaJpqlQueryHelper;
import org.eclipse.jpt.jpa.ui.JpaWorkbench;
import org.eclipse.jpt.jpa.ui.JptJpaUiImages;
import org.eclipse.jpt.jpa.ui.internal.plugin.JptJpaUiPlugin;
import org.eclipse.persistence.jpa.jpql.ContentAssistProposals;
import org.eclipse.persistence.jpa.jpql.WordParser;
import org.eclipse.persistence.jpa.jpql.parser.Expression;
import org.eclipse.persistence.jpa.jpql.parser.IdentifierRole;
import org.eclipse.persistence.jpa.jpql.spi.IEntity;
import org.eclipse.persistence.jpa.jpql.spi.IMapping;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * The abstract definition of JPQL content assist support.
 *
 * @version 3.3
 * @since 3.0
 * @author Pascal Filion
 */
@SuppressWarnings("nls")
abstract class JpqlCompletionProposalComputer<T> {

	/**
	 * The position of the cursor within the actual JPQL query (not modified).
	 */
	int actualPosition;

	/**
	 * The current value of the query element.
	 */
	String actualQuery;

	/**
	 * The provider of content assist items based on the position of the cursor within the JPQL query.
	 */
	ContentAssistProposals contentAssistProposals;

	/**
	 * The same value as {@link #actualQuery} or the modified query that was used by the Hermes parser.
	 */
	String jpqlQuery;

	/**
	 * The JPA model object that is used to access the Java project.
	 */
	NamedQuery namedQuery;

	/**
	 * The word before the position of the cursor.
	 */
	private String partialWord;

	/**
	 * The position of the cursor within the adjusted JPQL query.
	 */
	int position;

	/**
	 * This helper contains all the information necessary for retrieving the possible proposals
	 * required by content assist and to validate JPQL queries.
	 */
	JpaJpqlQueryHelper queryHelper;

	/**
	 * The end position of the query within the document.
	 */
	int tokenEnd;

	/**
	 * The start position of the query within the document.
	 */
	int tokenStart;

	/**
	 * Creates a new <code>JpqlCompletionProposalComputer</code>.
	 */
	JpqlCompletionProposalComputer() {
		super();
	}

	/**
	 * Adds completion proposals for the abstract schema names that are possible proposals.
	 *
	 * @param proposals The list used to store the new completion proposals
	 */
	private void addAbstractSchemaNames(List<T> proposals, ResourceManager resourceManager) {
		for (IEntity abstractSchemaType : sortByNames(contentAssistProposals.abstractSchemaTypes())) {
			proposals.add(buildAbstractSchemaNameProposal(abstractSchemaType, resourceManager));
		}
	}

	/**
	 * Adds completion proposals for the identification variables that are possible proposals.
	 *
	 * @param proposals The list used to store the new completion proposals
	 */
	private void addIdentificationVariables(List<T> proposals, ResourceManager resourceManager) {
		for (String variable : sort(contentAssistProposals.identificationVariables())) {
			proposals.add(buildIdentificationVariableProposal(variable, resourceManager));
		}
	}

	/**
	 * Adds completion proposals for the JPQL identifiers that are possible proposals.
	 *
	 * @param proposals The list used to store the new completion proposals
	 */
	private void addIdentifiers(List<T> proposals, ResourceManager resourceManager) {
		for (String identifier : sort(contentAssistProposals.identifiers())) {
			proposals.add(buildIdentifierProposal(identifier, resourceManager));
		}
	}

	private String additionalInfo(String proposal) {
		return JpqlIdentifierMessages.localizedMessage(proposal);
	}

	/**
	 * Adds completion proposals for the state fields and association fields that are possible proposals.
	 *
	 * @param proposals The list used to store the new completion proposals
	 */
	private void addMappings(List<T> proposals, ResourceManager resourceManager) {
		for (IMapping mapping : sort(contentAssistProposals.mappings())) {
			proposals.add(buildMappingProposal(mapping, resourceManager));
		}
	}

	private T buildAbstractSchemaNameProposal(IEntity abstractSchemaType, ResourceManager resourceManager) {
		String proposal = abstractSchemaType.getName();
		return buildProposal(proposal, proposal, resourceManager.createImage(JptJpaUiImages.ENTITY));
	}

	private Comparator<IEntity> buildEntityNameComparator() {
		return new Comparator<IEntity>() {
			public int compare(IEntity entity1, IEntity entity2) {
				return entity1.getName().compareTo(entity2.getName());
			}
		};
	}

	private String buildIdentificationVariableDisplayString(String identificationVariable) {

		IEntity abstractSchemaType = contentAssistProposals.getAbstractSchemaType(identificationVariable);

		if (abstractSchemaType != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(identificationVariable);
			sb.append(" : ");
			sb.append(abstractSchemaType.getName());
			identificationVariable = sb.toString();
		}

		return identificationVariable;
	}

	private T buildIdentificationVariableProposal(String proposal, ResourceManager resourceManager) {
		return buildProposal(
			proposal,
			buildIdentificationVariableDisplayString(proposal),
			resourceManager.createImage(JptJpaUiImages.JPQL_VARIABLE)
		);
	}

	private T buildIdentifierProposal(String proposal, ResourceManager resourceManager) {

		String additionalInfo = additionalInfo(proposal);
		IdentifierRole role = queryHelper.getQueryContext().getExpressionRegistry().getIdentifierRole(proposal);
		boolean realFunction = (role == IdentifierRole.FUNCTION) && isRealFunction(proposal);
		int cursorOffset = 0;

		// There is at least one letter before the cursor, if the setting "Match First Letter Case"
		// is true, then match the case of the JPQL identifier with the first character
		if ((partialWord.length() > 0) && shouldMatchFirstCharacterCase()) {
			if (Character.isLowerCase(partialWord.charAt(0))) {
				proposal = proposal.toLowerCase();
			}
		}
		// Convert the case of the JPQL identifier based on the setting
		else if (shouldUseLowercaseIdentifiers()) {
			proposal = proposal.toLowerCase();
		}

		// For JPQL function, we add () to the display string, example: AVG()
		// But for TRUE, FALSE, etc, we don't add ()
		if (realFunction) {
			proposal += "()";
			cursorOffset--;
		}

		Image image = resourceManager.createImage(realFunction ? JptJpaUiImages.JPQL_FUNCTION : JptJpaUiImages.JPQL_IDENTIFIER);
		return buildProposal(
			proposal,
			proposal,
			additionalInfo,
			image,
			cursorOffset
		);
	}

	private T buildMappingProposal(IMapping mapping, ResourceManager resourceManager) {
		String proposal = mapping.getName();
		Image image = resourceManager.createImage(mappingImageDescriptor(mapping));
		return buildProposal(proposal, proposal, image);
	}

	private T buildProposal(String proposal, String displayString, Image image) {
		return buildProposal(proposal, displayString, null, image, 0);
	}

	/**
	 * Creates a new completion proposal for the given proposal.
	 *
	 * @param proposal A valid proposal that can be inserted into the query
	 * @param displayString The human readable string of the proposal
	 * @param additionalInfo Optional additional information about the proposal. The additional
	 * information will be presented to assist the user in deciding if the selected proposal is the
	 * desired choice
	 * @param image The image that represents the proposal
	 * @param cursorOffset An offset that moves the cursor backward or forward after it is adjusted
	 * based on the given proposal
	 * @return The completion proposal
	 */
	abstract T buildProposal(String proposal,
	                         String displayString,
	                         String additionalInfo,
	                         Image image,
	                         int cursorOffset);

	/**
	 * Creates the list of completion proposals based on the current content of the JPQL query and at
	 * the specified position.
	 *
	 * @param namedQuery The model object used to access the application metadata information
	 * @param actualQuery The string representation of the JPQL query that is coming from the
	 * document itself (Java source or XML)
	 * @param offset The beginning of the string within the document
	 * @param position The position of the cursor within the query, which starts at the beginning of
	 * that query and not the document
	 * @return The list of completion proposals
	 */
	final List<T> buildProposals(NamedQuery namedQuery,
	                             String actualQuery,
	                             int tokenStart,
	                             int tokenEnd,
	                             int position,
	                             ResourceManager resourceManager) {

		try {
			this.tokenStart     = tokenStart;
			this.tokenEnd       = tokenEnd;
			this.actualQuery    = actualQuery;
			this.actualPosition = position;
			this.namedQuery     = namedQuery;

			// It's possible the string has literal representation of the escape characters, if required,
			// convert them into actual escape characters and adjust the position accordingly
			int[] positions  = { position };
			this.jpqlQuery   = modifyJpqlQuery(actualQuery, positions);
			this.position    = positions[0];
			this.partialWord = partialWord();

			// Create the query helper, initialize it and then retrieve the content assist proposals
			if (this.queryHelper == null) {
				this.queryHelper = namedQuery.getPersistenceUnit().createJpqlQueryHelper();
			}

			this.queryHelper.setQuery(namedQuery, jpqlQuery);
			this.contentAssistProposals = queryHelper.buildContentAssistProposals(positions[0]);

			// Create the proposals for those proposals
			List<T> proposals = new ArrayList<T>();
			addAbstractSchemaNames    (proposals, resourceManager);
			addIdentificationVariables(proposals, resourceManager);
			addIdentifiers            (proposals, resourceManager);
			addMappings               (proposals, resourceManager);

			return proposals;
		}
		finally {
			if (queryHelper != null) {
				queryHelper.dispose();
			}
			clearInformation();
		}
	}

	final void checkCanceled(IProgressMonitor monitor) throws InterruptedException {
		if (monitor.isCanceled()) {
			throw new InterruptedException();
		}
	}

	/**
	 * Clears the cached information.
	 */
	final void clearInformation() {
		namedQuery  = null;
		tokenStart  = -1;
		tokenEnd    = -1;
		position    = -1;
		actualQuery = null;
		namedQuery  = null;
		partialWord = null;
		contentAssistProposals = null;
	}

	/**
	 * Returns the reason why this computer was unable to produce any completion proposals or
	 * context information.
	 *
	 * @return An error message or <code>null</code> if no error occurred
	 */
	public String getErrorMessage() {
		return null;
	}

	ResourceManager getResourceManager(Control control) {
		return this.getJpaWorkbench().getResourceManager(control);
	}

	JpaWorkbench getJpaWorkbench() {
		return PlatformTools.getAdapter(PlatformUI.getWorkbench(), JpaWorkbench.class);
	}

	private boolean isRealFunction(String identifier) {
		return identifier != Expression.TRUE         &&
		       identifier != Expression.FALSE        &&
		       identifier != Expression.NULL         &&
		       identifier != Expression.CURRENT_DATE &&
		       identifier != Expression.CURRENT_TIME &&
		       identifier != Expression.CURRENT_TIMESTAMP;
	}

	private ImageDescriptor mappingImageDescriptor(IMapping mapping) {
		switch (mapping.getMappingType()) {
			case BASIC:               return JptJpaUiImages.BASIC;
//			case BASIC_COLLECTION:    return JptJpaUiImages.ELEMENT_COLLECTION;
//			case BASIC_MAP:           return JptJpaUiImages.ELEMENT_COLLECTION;
			case ELEMENT_COLLECTION:  return JptJpaUiImages.ELEMENT_COLLECTION;
			case EMBEDDED:            return JptJpaUiImages.EMBEDDED;
			case EMBEDDED_ID:         return JptJpaUiImages.EMBEDDED_ID;
			case ID:                  return JptJpaUiImages.ID;
			case MANY_TO_MANY:        return JptJpaUiImages.MANY_TO_MANY;
			case MANY_TO_ONE:         return JptJpaUiImages.MANY_TO_ONE;
			case ONE_TO_MANY:         return JptJpaUiImages.ONE_TO_MANY;
			case ONE_TO_ONE:          return JptJpaUiImages.ONE_TO_ONE;
//			case TRANSFORMATION:      return JptJpaUiImages.BASIC;      // TODO
//			case VARIABLE_ONE_TO_ONE: return JptJpaUiImages.ONE_TO_ONE; // TODO
			case VERSION:             return JptJpaUiImages.VERSION;
			default:                  return JptJpaUiImages.TRANSIENT;
		}
	}

	/**
	 * In some context, the given JPQL query needs to be modified before it is parsed.
	 *
	 * @param jpqlQuery The JPQL query to keep unchanged or to modify before parsing it
	 * @param position The position of the cursor within the JPQL query, which needs to be updated if
	 * the query is modified
	 * @return The given JPQL query or a modified version that will be parsed
	 */
	String modifyJpqlQuery(String jpqlQuery, int[] position) {
		return jpqlQuery;
	}

	private String partialWord() {
		WordParser wordParser = new WordParser(jpqlQuery);
		wordParser.setPosition(position);
		return wordParser.partialWord();
	}

	/**
	 * Informs the computer that a content assist session has ended.
	 */
	public void sessionEnded() {

		queryHelper = null;
		clearInformation();
	}

	/**
	 * Informs the computer that a content assist session has started.
	 */
	public void sessionStarted() {
		// Nothing to do
	}

	private boolean shouldMatchFirstCharacterCase() {
		return JpaPreferences.getJpqlIdentifierMatchFirstCharacterCase();
	}

	private boolean shouldUseLowercaseIdentifiers() {
		return JpaPreferences.getJpqlIdentifierLowercase();
	}

	private <I extends Comparable<? super I>> Iterable<I> sort(Iterable<I> iterator) {
		return IterableTools.sort(iterator);
	}

	private Iterable<IEntity> sortByNames(Iterable<IEntity> abstractSchemaTypes) {
		return IterableTools.sort(abstractSchemaTypes, buildEntityNameComparator());
	}
}
