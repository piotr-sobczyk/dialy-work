/*
 * Copyright (c) 2010-2012 Célio Cidral Junior.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.dailywork.ui.state.bursts;

import java.awt.Component;

import javax.swing.Action;

import org.dailywork.ui.state.ToState;
import org.dailywork.ui.state.UiStateSupport;
import org.dailywork.ui.state.breaks.Break;

public class BurstInterrupted extends UiStateSupport {

    @Override
    protected String title() {
        return null;
    }

    @Override
    protected Component createContent() {
        return labelFactory.medium(messages.get("Burst interrupted"));
    }

    @Override
    protected Action[] primaryActions() {
        return new Action[] {
                new ToState(messages.get("Restart"), Burst.class)
        };
    }

    @Override
    protected Action[] secondaryActions() {
        return new Action[] {
                new ToState(messages.get("Break"), Break.class)
        };
    }

}
