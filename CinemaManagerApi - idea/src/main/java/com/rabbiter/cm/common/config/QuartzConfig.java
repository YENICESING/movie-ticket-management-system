package com.rabbiter.cm.common.config;


import com.rabbiter.cm.common.utils.ApplicationContextUtils;
import com.rabbiter.cm.service.impl.SysBillServiceImpl;
import com.rabbiter.cm.service.impl.SysSessionServiceImpl;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
/////该代码使用 Spring 和 Quartz 构建了一个订单和场次管理系统，通过服务实现类处理业务逻辑，
// 使用定时任务来处理超时订单。但在代码的细节上存在一些需要优化和完善的地方，如定时任务的业务逻辑处理、服务实现类的方法优化和异常处理，
// 以提高系统的性能、可维护性和健壮性。同时，代码中的一些功能还需要进一步完善，
// 如 SysBillMapper 和 SysSessionMapper 的具体实现（在 MyBatis 的 XML 映射文件中）以及 QuartzConfig 中定时任务的详细业务逻辑处理。
@Configuration
public class QuartzConfig {

    @Autowired
    private SysBillServiceImpl sysBillService;

    @Autowired
    private SysSessionServiceImpl sysSessionService;

    @Bean
    public JobDetail jobDetail() {
        QuartzJobBean quartzJob = new QuartzJobBean() {
            @Override
            protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("执行取消超时订单处理" + sdf.format(new Date()));
                // 取消所有超时订单并释放占座资源
                ApplicationContextUtils.getBean("cancelTimeoutBill");
            }
        };
        return JobBuilder.newJob(quartzJob.getClass()).storeDurably().build();
    }

    @Bean
    public SimpleTrigger trigger() {
        //每5分钟执行一次，一直重复执行
        SimpleScheduleBuilder scheduleBuilder =
                SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5 * 60)
                        .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(scheduleBuilder).build();
    }

}
