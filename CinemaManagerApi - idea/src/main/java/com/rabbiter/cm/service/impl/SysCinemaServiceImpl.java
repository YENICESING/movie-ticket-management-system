package com.rabbiter.cm.service.impl;

import com.rabbiter.cm.domain.SysCinema;
import com.rabbiter.cm.mapper.SysCinemaMapper;
import com.rabbiter.cm.service.SysCinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

////该类实现了 SysCinemaService 接口，实现了接口中定义的影院服务的业务逻辑
@Service
public class SysCinemaServiceImpl implements SysCinemaService {

    @Autowired
    private SysCinemaMapper sysCinemaMapper;

    @Override
    public SysCinema findCinema() {
        return sysCinemaMapper.findCinema();
    }

    @Override
    public int updateCinema(SysCinema sysCinema) {
        return sysCinemaMapper.updateCinema(sysCinema);
    }

    @Override
    public SysCinema findCinemaById(Long id) {
        return sysCinemaMapper.findCinemaById(id);
    }
}
