/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package upload;

import com.voicetribe.util.code.Code;
import com.voicetribe.util.code.Log4JCodeListenerFactory;
import com.voicetribe.wicket.WebApplication;

/**
 * HttpApplication class for upload example.
 * @author Eelco Hillenius
 */
public class UploadApplication extends WebApplication
{
    /**
     * Constructor.
     */
    public UploadApplication()
    {
        Code.addListenerFactory(new Log4JCodeListenerFactory());
        getSettings().setHomePage(UploadPage.class);
//        Duration pollFreq = Duration.ONE_SECOND;
//        getSettings().setResourcePollFrequency(pollFreq);
    }
}

///////////////////////////////// End of File /////////////////////////////////
