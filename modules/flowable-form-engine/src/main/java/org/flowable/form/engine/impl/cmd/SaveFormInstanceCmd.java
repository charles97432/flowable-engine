/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.flowable.form.engine.impl.cmd;

import java.util.List;
import java.util.Map;

import org.flowable.form.api.FormInstance;
import org.flowable.form.api.FormInstanceQuery;
import org.flowable.form.engine.FormEngineConfiguration;
import org.flowable.form.engine.impl.persistence.entity.FormInstanceEntity;
import org.flowable.form.model.FormModel;

public class SaveFormInstanceCmd extends AbstractSaveFormInstanceCmd {

    private static final long serialVersionUID = 1L;

    public SaveFormInstanceCmd(FormModel formModel, Map<String, Object> variables, String taskId, String processInstanceId) {
        super(formModel, variables, taskId, processInstanceId);
    }

    public SaveFormInstanceCmd(String formModelId, Map<String, Object> variables, String taskId, String processInstanceId) {
        super(formModelId, variables, taskId, processInstanceId);
    }

    protected FormInstanceEntity findExistingFormInstance(FormEngineConfiguration formEngineConfiguration) {

        if (taskId == null) {
            // Only update formInstances related to a task - cannot save a process start form as no processInstance exists
            return null;
        }

        FormInstanceQuery formInstanceQuery =
                formEngineConfiguration.getFormService().createFormInstanceQuery().formDefinitionId(formModel.getId()).taskId(taskId);

        List<FormInstance> formInstances = formInstanceQuery.orderBySubmittedDate().desc().list();

        if (formInstances.size() > 0 && formInstances.get(0) instanceof FormInstanceEntity) {
            return (FormInstanceEntity) formInstances.get(0);
        }
        return null;
    }
}