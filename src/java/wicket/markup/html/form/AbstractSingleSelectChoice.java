/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.form;

import java.util.List;

import wicket.model.IModel;
import wicket.util.string.AppendingStringBuffer;

/**
 * Abstract base class for single-select choices.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Johan Compagner
 */
abstract class AbstractSingleSelectChoice extends AbstractChoice
{
	/** String to display when the selected value is null and nullValid is false. */
	private static final String CHOOSE_ONE = "Choose One";

	private static final String NO_SELECTION_VALUE = "-1";

	private static final String EMPTY_STRING = "";

	/** Is the null value a valid value? */
	private boolean nullValid = false;

	/**
	 * @see AbstractChoice#AbstractChoice(String)
	 */
	public AbstractSingleSelectChoice(final String id)
	{
		super(id);
	}

	/**
	 * @see AbstractChoice#AbstractChoice(String, List)
	 */
	public AbstractSingleSelectChoice(final String id, final List choices)
	{
		super(id, choices);
	}

	/**
	 * @param id
	 * @param data
	 * @param renderer
	 * @see AbstractChoice#AbstractChoice(String, List ,IChoiceRenderer)
	 */
	public AbstractSingleSelectChoice(final String id, final List data,
			final IChoiceRenderer renderer)
	{
		super(id, data, renderer);
	}

	/**
	 * @see AbstractChoice#AbstractChoice(String, IModel, List)
	 */
	public AbstractSingleSelectChoice(final String id, IModel model, final List data)
	{
		super(id, model, data);
	}

	/**
	 * @param id
	 * @param model
	 * @param data
	 * @param renderer
	 * @see AbstractChoice#AbstractChoice(String, IModel, List, IChoiceRenderer)
	 */
	public AbstractSingleSelectChoice(final String id, IModel model, final List data,
			final IChoiceRenderer renderer)
	{
		super(id, model, data, renderer);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel)
	 */
	public AbstractSingleSelectChoice(String id, IModel choices)
	{
		super(id, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel,IModel)
	 */
	public AbstractSingleSelectChoice(String id, IModel model, IModel choices)
	{
		super(id, model, choices);
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel,IChoiceRenderer)
	 */
	public AbstractSingleSelectChoice(String id, IModel choices, IChoiceRenderer renderer)
	{
		super(id, choices, renderer);
	}


	/**
	 * @see wicket.markup.html.form.AbstractChoice#AbstractChoice(String,
	 *      IModel, IModel,IChoiceRenderer)
	 */
	public AbstractSingleSelectChoice(String id, IModel model, IModel choices,
			IChoiceRenderer renderer)
	{
		super(id, model, choices, renderer);
	}

	/**
	 * @see FormComponent#getModelValue()
	 */
	public final String getModelValue()
	{
		final Object object = getModelObject();
		if (object != null)
		{
			int index = getChoices().indexOf(object);
			return getChoiceRenderer().getIdValue(object, index);
		}
		return NO_SELECTION_VALUE;
	}

	/**
	 * Is the <code>null</code> value a valid value?
	 * 
	 * @return <code>true</code> when the <code>null</code> value is
	 *         allowed.
	 */
	public boolean isNullValid()
	{
		return nullValid;
	}

	/**
	 * Is the <code>null</code> value a valid value?
	 * 
	 * @param nullValid
	 *            whether null is a valid value
	 */
	public void setNullValid(boolean nullValid)
	{
		this.nullValid = nullValid;
	}

	/**
	 * @see wicket.markup.html.form.FormComponent#convertValue(java.lang.String)
	 */
	protected final Object convertValue(final String value)
	{
		List choices = getChoices();
		for (int index = 0; index < choices.size(); index++)
		{
			// Get next choice
			final Object choice = choices.get(index);
			if (getChoiceRenderer().getIdValue(choice, index).equals(value))
			{
				return choice;
			}
		}
		return null;
	}

	/**
	 * @see wicket.markup.html.form.AbstractChoice#getDefaultChoice(Object)
	 */
	protected CharSequence getDefaultChoice(final Object selected)
	{
		// Is null a valid selection value?
		if (isNullValid())
		{
			// Null is valid, so look up the value for it
			final String option = getLocalizer().getString("null", this, "");

			// The <option> tag buffer
			final AppendingStringBuffer buffer = new AppendingStringBuffer(32 + option.length());


			// Add option tag
			buffer.append("\n<option");

			// If null is selected, indicate that
			if (selected == null)
			{
				buffer.append(" selected=\"selected\"");
			}

			// Add body of option tag
			buffer.append(" value=\"\">").append(option).append("</option>");
			return buffer;
		}
		else
		{
			// Null is not valid. Is it selected anyway?
			if (selected == null || selected.equals(NO_SELECTION_VALUE)
					|| selected.equals(EMPTY_STRING))
			{
				// Force the user to pick a non-null value
				final String option = getLocalizer().getString("null", this, CHOOSE_ONE);
				return new AppendingStringBuffer("\n<option selected=\"selected\" value=\"\">").append(option).append(
						"</option>");
			}
		}
		return "";
	}


	/*
	 * Gets whether the given value represents the current selection.
	 * 
	 * 
	 * aram object The object to check @param index The index of the object in
	 * the collection @param selected The current selected id value @return
	 * Whether the given value represents the current selection
	 */
	protected boolean isSelected(final Object object, int index, String selected)
	{
		return selected != null && selected.equals(getChoiceRenderer().getIdValue(object, index));
	}
}