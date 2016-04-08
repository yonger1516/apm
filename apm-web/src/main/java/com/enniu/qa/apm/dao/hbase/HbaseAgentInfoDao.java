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

package com.enniu.qa.apm.dao.hbase;

import com.enniu.qa.apm.dao.AgentInfoDao;
import com.enniu.qa.apm.model.PinpointAgentInfo;
import com.navercorp.pinpoint.common.bo.AgentInfoBo;
import com.navercorp.pinpoint.common.bo.ServerMetaDataBo;
import com.navercorp.pinpoint.common.bo.ServiceInfoBo;
import com.navercorp.pinpoint.common.buffer.Buffer;
import com.navercorp.pinpoint.common.buffer.FixedBuffer;
import com.navercorp.pinpoint.common.hbase.HBaseTables;
import com.navercorp.pinpoint.common.hbase.HbaseOperations2;
import com.navercorp.pinpoint.common.service.ServiceTypeRegistryService;
import com.navercorp.pinpoint.common.util.BytesUtils;
import com.navercorp.pinpoint.common.util.RowKeyUtils;
import com.navercorp.pinpoint.common.util.TimeUtils;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author emeroad
 * @author HyunGil Jeong
 */
@Repository
public class HbaseAgentInfoDao implements AgentInfoDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HbaseOperations2 hbaseOperations2;

	@Autowired
	private ServiceTypeRegistryService registry;

	@Override
	public void insert(PinpointAgentInfo agentInfo) {
		if (agentInfo == null) {
			throw new NullPointerException("agentInfo must not be null");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("insert agent info. {}", agentInfo);
		}

		byte[] agentId = Bytes.toBytes(agentInfo.getAgentId());
		long reverseKey = TimeUtils.reverseTimeMillis(agentInfo.getStartTimestamp());
		byte[] rowKey = RowKeyUtils.concatFixedByteAndLong(agentId, HBaseTables.AGENT_NAME_MAX_LEN, reverseKey);
		Put put = new Put(rowKey);

		// should add additional agent informations. for now added only starttime for sqlMetaData
		AgentInfoBo agentInfoBo = this.map(agentInfo);
		byte[] agentInfoBoValue = agentInfoBo.writeValue();
		put.addColumn(HBaseTables.AGENTINFO_CF_INFO, HBaseTables.AGENTINFO_CF_INFO_IDENTIFIER, agentInfoBoValue);

		if (null!=agentInfo.getServerMetaData()) {
			ServerMetaDataBo serverMetaDataBo = agentInfo.getServerMetaData();
			byte[] serverMetaDataBoValue = serverMetaDataBo.writeValue();
			put.addColumn(HBaseTables.AGENTINFO_CF_INFO, HBaseTables.AGENTINFO_CF_INFO_SERVER_META_DATA, serverMetaDataBoValue);
		}

		hbaseOperations2.put(HBaseTables.AGENTINFO, put);
	}

	public AgentInfoBo map(PinpointAgentInfo agentInfo) {
		final String hostName = agentInfo.getHostName();
		final String ip = agentInfo.getIp();
		final String ports = agentInfo.getPorts();
		final String agentId = agentInfo.getAgentId();
		final String applicationName = agentInfo.getApplicationName();
		final short serviceType = agentInfo.getServiceTypeCode();
		final int pid = agentInfo.getPid();
		final String vmVersion = agentInfo.getVmVersion();
		final String agentVersion = agentInfo.getAgentVersion();
		final long startTime = agentInfo.getStartTimestamp();
		final long endTimeStamp = agentInfo.getStartTimestamp();
		final int endStatus = 1;

		AgentInfoBo.Builder builder = new AgentInfoBo.Builder();
		builder.setHostName(hostName);
		builder.setIp(ip);
		builder.setPorts(ports);
		builder.setAgentId(agentId);
		builder.setApplicationName(applicationName);
		builder.setServiceTypeCode(serviceType);
		builder.setPid(pid);
		builder.setVmVersion(vmVersion);
		builder.setAgentVersion(agentVersion);
		builder.setStartTime(startTime);
		builder.setEndTimeStamp(endTimeStamp);
		builder.setEndStatus(endStatus);

		return builder.build();
	}

	/**
	 * Returns the information of the agent with its start time closest to the given timestamp
	 *
	 * @param agentId
	 * @param timestamp
	 * @return
	 */
	@Override
	public AgentInfoBo getAgentInfo(final String agentId, final long timestamp) {
		if (agentId == null) {
			throw new NullPointerException("agentId must not be null");
		}

		// TODO need to be cached
		Scan scan = createScan(agentId, timestamp);
		scan.setMaxVersions(1);
		scan.setCaching(1);

		AgentInfoBo result = this.hbaseOperations2.find(HBaseTables.AGENTINFO, scan, new AgentInfoBoResultsExtractor(agentId));
		if (result == null) {
			logger.warn("agentInfo not found. agentId={}, time={}", agentId, timestamp);
		}
		return result;
	}

	/**
	 * Returns the very first information of the agent
	 *
	 * @param agentId
	 */
	@Override
	public AgentInfoBo getInitialAgentInfo(final String agentId) {
		if (agentId == null) {
			throw new NullPointerException("agentId must not be null");
		}
		Scan scan = new Scan();
		byte[] agentIdBytes = Bytes.toBytes(agentId);
		byte[] reverseStartKey = RowKeyUtils.concatFixedByteAndLong(agentIdBytes, HBaseTables.AGENT_NAME_MAX_LEN, Long.MAX_VALUE);
		scan.setStartRow(reverseStartKey);
		scan.setReversed(true);
		scan.setMaxVersions(1);
		scan.setCaching(1);

		AgentInfoBo result = this.hbaseOperations2.find(HBaseTables.AGENTINFO, scan, new AgentInfoBoResultsExtractor(agentId));
		if (result == null) {
			logger.warn("agentInfo not found. agentId={}, time={}", agentId, 0);
		}
		return result;
	}

	public List<AgentInfoBo> getAllAgentInfo(){
		List<AgentInfoBo> infoBos=new ArrayList<AgentInfoBo>();

		Scan scan=new Scan();
		scan.setCaching(Integer.MAX_VALUE);
		hbaseOperations2.find(HBaseTables.AGENTINFO, scan, new AgentInfoBoResultListExtractor());

		return infoBos;
	}

	private class AgentInfoBoResultListExtractor implements ResultsExtractor<List<AgentInfoBo>> {

		@Override
		public List<AgentInfoBo> extractData(ResultScanner results) throws Exception {
			List<AgentInfoBo> list=new ArrayList<AgentInfoBo>();
			for (Result next : results) {
				byte[] row = next.getRow();
				String agentId=new String(getAgentIdBytes(row,HBaseTables.AGENT_NAME_MAX_LEN));

				logger.debug("agentId:{}", agentId);

				//list.add(agentInfoBo);
			}
			return list;
		}
	}


	public byte[] getAgentIdBytes(byte[] row,int len){
		byte[] newBytes=new byte[len];

		for(int i=0;i<len;i++){
			newBytes[i]=row[i];
		}

		return newBytes;
	}

	private class AgentInfoBoResultsExtractor implements ResultsExtractor<AgentInfoBo> {

		private final String agentId;

		private AgentInfoBoResultsExtractor(String agentId) {
			this.agentId = agentId;
		}

		@Override
		public AgentInfoBo extractData(ResultScanner results) throws Exception {
			for (Result next : results) {
				byte[] row = next.getRow();

				long reverseStartTime = BytesUtils.bytesToLong(row, HBaseTables.AGENT_NAME_MAX_LEN);
				long startTime = TimeUtils.recoveryTimeMillis(reverseStartTime);

				byte[] serializedAgentInfo = next.getValue(HBaseTables.AGENTINFO_CF_INFO, HBaseTables.AGENTINFO_CF_INFO_IDENTIFIER);
				byte[] serializedServerMetaData = next.getValue(HBaseTables.AGENTINFO_CF_INFO, HBaseTables.AGENTINFO_CF_INFO_SERVER_META_DATA);

				final AgentInfoBo.Builder agentInfoBoBuilder = createBuilderFromValue(serializedAgentInfo);
				agentInfoBoBuilder.setAgentId(this.agentId);
				agentInfoBoBuilder.setStartTime(startTime);

				if (serializedServerMetaData != null) {
					agentInfoBoBuilder.setServerMetaData(new ServerMetaDataBo.Builder(serializedServerMetaData).build());
				}
				final AgentInfoBo agentInfoBo = agentInfoBoBuilder.build();

				logger.debug("agent:{} startTime value {}", agentId, startTime);

				return agentInfoBo;
			}

			return null;
		}

		private AgentInfoBo.Builder createBuilderFromValue(byte[] serializedAgentInfo) {
			final Buffer buffer = new FixedBuffer(serializedAgentInfo);
			final AgentInfoBo.Builder builder = new AgentInfoBo.Builder();
			builder.setHostName(buffer.readPrefixedString());
			builder.setIp(buffer.readPrefixedString());
			builder.setPorts(buffer.readPrefixedString());
			builder.setApplicationName(buffer.readPrefixedString());
			builder.setServiceTypeCode(buffer.readShort());
			builder.setPid(buffer.readInt());
			builder.setAgentVersion(buffer.readPrefixedString());
			builder.setStartTime(buffer.readLong());
			builder.setEndTimeStamp(buffer.readLong());
			builder.setEndStatus(buffer.readInt());
			// FIXME - 2015.09 v1.5.0 added vmVersion (check for compatibility)
			if (buffer.limit() > 0) {
				builder.setVmVersion(buffer.readPrefixedString());
			}
			return builder;
		}

	}

	private Scan createScan(String agentId, long currentTime) {
		Scan scan = new Scan();

		byte[] agentIdBytes = Bytes.toBytes(agentId);
		long startTime = TimeUtils.reverseTimeMillis(currentTime);
		byte[] startKeyBytes = RowKeyUtils.concatFixedByteAndLong(agentIdBytes, HBaseTables.AGENT_NAME_MAX_LEN, startTime);
		byte[] endKeyBytes = RowKeyUtils.concatFixedByteAndLong(agentIdBytes, HBaseTables.AGENT_NAME_MAX_LEN, Long.MAX_VALUE);

		scan.setStartRow(startKeyBytes);
		scan.setStopRow(endKeyBytes);
		scan.addFamily(HBaseTables.AGENTINFO_CF_INFO);

		return scan;
	}

}
