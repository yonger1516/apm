/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iyonger.apm.web.dao;

import com.iyonger.apm.web.model.PinpointAgentInfo;
import com.navercorp.pinpoint.common.bo.AgentInfoBo;

import java.util.List;

/**
 * @author emeroad
 * @author HyunGil Jeong
 */
public interface AgentInfoDao {
    
    AgentInfoBo getAgentInfo(String agentId, long timestamp);
    
    AgentInfoBo getInitialAgentInfo(String agentId);
    void insert(PinpointAgentInfo agentInfo);

    List<AgentInfoBo> getAllAgentInfo();
}