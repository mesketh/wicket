/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html.pages;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.settings.IRequestCycleSettings;

/**
 * This page uses a form post right after the page has loaded in the browser, using JavaScript or
 * alternative means to detect and pass on settings to the embedded form. The form submit method
 * updates this session's {@link org.apache.wicket.core.request.ClientInfo} object and then redirects to
 * the original location as was passed in as a URL argument in the constructor.
 * </p>
 * <p>
 * This page is being used by the default implementation of {@link org.apache.wicket.Session#getClientInfo()},
 * which in turn uses {@link IRequestCycleSettings#getGatherExtendedBrowserInfo() a setting} to
 * determine whether this page should be redirected to (it does when it is true).
 * </p>
 * 
 * @author Eelco Hillenius
 */
public class BrowserInfoPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	private BrowserInfoForm browserInfoForm;

	/**
	 * This constructor will be called twice, once to display the initial page and additionally when JavaScript
	 * is not enabled and the meta refresh fires or the user clicks the link instead.
	 */
	public BrowserInfoPage()
	{
		initComps();
		RequestCycle requestCycle = getRequestCycle();
		WebSession session = (WebSession)getSession();

		WebClientInfo clientInfo = session.getClientInfo();
		if (clientInfo != null)
		{
			// clientInfo is already present so continue
			continueToOriginalDestination();
			
			// switch to home page if no original destination was intercepted
			setResponsePage(getApplication().getHomePage());
		}
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.render(OnLoadHeaderItem.forScript(
				String.format("Wicket.BrowserInfo.submitForm('%s')", browserInfoForm.getFormMarkupId())));
	}

	@Override
	public boolean isVersioned()
	{
		return false;
	}

	/**
	 * Adds components.
	 */
	private void initComps()
	{
		WebComponent meta = new WebComponent("meta");

		final IModel<String> urlModel = new LoadableDetachableModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String load()
			{
				CharSequence url = urlFor(BrowserInfoPage.class, null);
				return url.toString();
			}
		};

		meta.add(AttributeModifier.replace("content", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return "0; url=" + urlModel.getObject();
			}

		}));
		add(meta);
		WebMarkupContainer link = new WebMarkupContainer("link");
		link.add(AttributeModifier.replace("href", urlModel));
		add(link);

		browserInfoForm = new BrowserInfoForm("postback")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void afterSubmit()
			{
				continueToOriginalDestination();
			}
		};
		add(browserInfoForm);
	}
}
