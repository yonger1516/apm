package com.iyonger.apm.web;

import com.iyonger.apm.web.dao.hbase.HbaseAgentInfoDao;
import com.iyonger.apm.web.model.PinpointAgentInfo;
import com.navercorp.pinpoint.common.bo.AgentInfoBo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.util.Date;
import java.util.List;

/**
 * Created by fuyong on 3/14/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PtmWebMain.class)
public class HBaseDaoTest extends AbstractTestNGSpringContextTests {

	@Autowired
	HbaseAgentInfoDao agentInfoDao;

	@Test
	public void testAgentInfo(){
		String agentId="50";
		long ts=new Date().getTime();
		System.out.println(agentInfoDao.getAgentInfo(agentId,ts));
	}

	@Test
	public void testInsertAgentInfo(){
		PinpointAgentInfo agentInfo=new PinpointAgentInfo();
		agentInfo.setAgentId("53");
		agentInfo.setApplicationName("apm-test1");

		try{
			agentInfoDao.insert(agentInfo);
		}catch (Exception e){
			Assert.assertTrue(false);
		}

	}

	@Test
	public void testGetAllAgentInfos(){
		List<AgentInfoBo> list=agentInfoDao.getAllAgentInfo();
		for(AgentInfoBo bo:list){
			System.out.println(bo.toString());
		}
	}
}
