package com.enniu.qa.apm.service;

import com.enniu.qa.apm.dao.CommitDao;
import com.enniu.qa.apm.model.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.ngrinder.common.util.Preconditions.checkNotNull;

/**
 * Created by fuyong on 7/15/15.
 */
@Service
public class CommitService {
	@Autowired
	CommitDao dao;

	public void newCommit(Commit commit){
		dao.saveAndFlush(commit);
	}

	public void deleteCommit(long id){
		dao.delete(id);
	}

	public Commit findById(long id){
		return dao.findById(id);
	}

	public List<Commit> findByProjectId(int id){
		return dao.findByProjectId(id);
	}


	public Commit save(Commit commit){
		checkNotNull(commit);
		if (commit.exist()){
			Commit exist=dao.findById(commit.getId());
			commit=exist.merge(commit);
		}else{

		}
		return dao.saveAndFlush(commit);
	}

}

