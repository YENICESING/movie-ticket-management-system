package com.rabbiter.cm.service.impl;

import com.rabbiter.cm.domain.SysHall;
import com.rabbiter.cm.mapper.SysHallMapper;
import com.rabbiter.cm.service.SysHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

///////////作为服务层组件，它负责调用数据访问层的方法。例如，在 addHall 方法中，将添加厅信息的任务委托给 sysHallMapper.addHall(sysHall)，
// 服务层专注于业务逻辑的实现和服务调用，而不直接操作数据库，实现了服务层和数据访问层的解耦。
//在 deleteHall 方法中，使用循环遍历 sysHalls 数组，调用 sysHallMapper.deleteHall(sysHall) 方法删除多个厅信息，这对于批量删除操作很有用。
@Service
public class SysHallServiceImpl implements SysHallService {

    @Autowired
    private SysHallMapper sysHallMapper;

    @Override
    public List<SysHall> findAllHalls(SysHall sysHall) {
        return sysHallMapper.findAllHalls(sysHall);
    }

    @Override
    public SysHall findHallById(SysHall sysHall) {
        return sysHallMapper.findHallById(sysHall);
    }

    @Override
    public int addHall(SysHall sysHall) {
        return sysHallMapper.addHall(sysHall);
    }

    @Override
    public int updateHall(SysHall sysHall) {
        return sysHallMapper.updateHall(sysHall);
    }

    @Override
    public int deleteHall(SysHall[] sysHalls) {
        int rows = 0;
        for (SysHall sysHall : sysHalls) {
            rows += sysHallMapper.deleteHall(sysHall);
        }
        return rows;
    }
}
