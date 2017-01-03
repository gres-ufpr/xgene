/*
 * Copyright 2017 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.ufpr.gres.selection;

import br.ufpr.gres.core.MutationDetails;
import br.ufpr.gres.core.MutationIdentifier;
import java.util.List;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public interface ISelectionStrategy {    
    public boolean contains(MutationIdentifier id);
        
    public void remove(MutationIdentifier id);
    
    public void reset();
    
    public void update(MutationDetails t);
    public void updateIgnoreItem(MutationDetails t);
    public void updateListStrategy(MutationDetails t);
    public void updateListStrategy(List<MutationDetails> t);
    
    public void setMaxSelection(int num);
    public void setMinSelection(int num);
    
    public List<MutationDetails> get();           
    public MutationDetails get(MutationIdentifier id);
    public List<MutationDetails> getItemsIgnored();      
    
    public boolean allItemsSelected();
}
