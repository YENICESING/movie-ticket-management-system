package com.rabbiter.cm.service.impl;

import com.rabbiter.cm.domain.SysSession;
import com.rabbiter.cm.domain.vo.SysSessionVo;
import com.rabbiter.cm.mapper.SysSessionMapper;
import com.rabbiter.cm.service.SysSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

///////SysSessionServiceImpl 是一个服务实现类，实现了 SysSessionService 接口，主要用于处理与场次（Session）相关的业务逻辑操作。
// 它通过 @Autowired 注解注入了 SysSessionMapper 对象，将具体的数据操作委托给 SysSessionMapper 完成，实现了对场次信息的增删改查等功能。
@Service
public class SysSessionServiceImpl implements SysSessionService {

    @Autowired
    private SysSessionMapper sysSessionMapper;

    @Override
    public List<SysSession> findByVo(SysSessionVo sysSessionVo) {
        return sysSessionMapper.findByVo(sysSessionVo);
    }

    @Override
    public List<SysSession> findSessionByMovieIdOrHallId(SysSession sysSession) {
        return sysSessionMapper.findSessionByMovieIdOrHallId(sysSession);
    }

    @Override
    public SysSession findSessionById(Long id) {
        return sysSessionMapper.findSessionById(id);
    }

    @Override
    public SysSession findOneSession(Long id) {
        return sysSessionMapper.findOneSession(id);
    }

    @Override
    public int addSession(SysSession sysSession) {
        return sysSessionMapper.addSession(sysSession);
    }

    @Override
    public int updateSession(SysSession sysSession) {
        return sysSessionMapper.updateSession(sysSession);
    }

    @Override
    public int deleteSession(Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += sysSessionMapper.deleteSession(id);
        }
        return rows;
    }

    @Override
    public List<SysSession> findSessionByMovieId(Long movieId) {
        return sysSessionMapper.findSessionByMovieId(movieId);
    }
}
