/**
 * Mogwai ERDesigner. Copyright (C) 2002 The Mogwai Project.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package de.erdesignerng.model.serializer.xml10;

import de.erdesignerng.model.Index;
import de.erdesignerng.model.IndexExpression;
import de.erdesignerng.model.IndexType;
import de.erdesignerng.model.Table;
import de.erdesignerng.model.serializer.AbstractXMLIndexSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLIndexSerializer extends AbstractXMLIndexSerializer {

	@Override
	public void serialize(Index aIndex, Document aDocument, Element aRootElement) {
		Element theIndexElement = addElement(aDocument, aRootElement, INDEX);

		theIndexElement.setAttribute(INDEXTYPE, aIndex.getIndexType().toString());

		serializeProperties(aDocument, theIndexElement, aIndex);

		// Attribute
		for (IndexExpression theAttribute : aIndex.getExpressions()) {
			Element theAttributeElement = addElement(aDocument, theIndexElement, INDEXATTRIBUTE);
			theAttributeElement.setAttribute(ATTRIBUTEREFID, theAttribute.getAttributeRef().getSystemId());
		}

	}

	@Override
	public void deserialize(Table aTable, Element aElement) {
		// Parse the indexes
		NodeList theIndexes = aElement.getElementsByTagName(INDEX);
		for (int j = 0; j < theIndexes.getLength(); j++) {

			Element theIndexElement = (Element) theIndexes.item(j);
			Index theIndex = new Index();
			theIndex.setOwner(aTable);
			deserializeProperties(theIndexElement, theIndex);

			theIndex.setIndexType(IndexType.fromString(theIndexElement.getAttribute(INDEXTYPE)));

			NodeList theAttributes = theIndexElement.getElementsByTagName(INDEXATTRIBUTE);
			for (int k = 0; k < theAttributes.getLength(); k++) {

				Element theAttributeElement = (Element) theAttributes.item(k);

				String theAttributeRefId = theAttributeElement.getAttribute(ATTRIBUTEREFID);
				IndexExpression theExpression = new IndexExpression();
				theExpression.setAttributeRef(aTable.getAttributes().findBySystemId(theAttributeRefId));
				theIndex.getExpressions().add(theExpression);
			}

			aTable.getIndexes().add(theIndex);

		}
	}
}