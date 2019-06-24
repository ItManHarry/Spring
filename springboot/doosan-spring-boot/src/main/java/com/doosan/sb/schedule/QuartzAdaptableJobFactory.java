package com.doosan.sb.schedule;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;
@Component("jobFactory")
public class QuartzAdaptableJobFactory extends AdaptableJobFactory {
	@Autowired
	private AutowireCapableBeanFactory factory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		//创建job对象
		Object jobInstance = super.createJobInstance(bundle);
		//放入Spring容器
		factory.autowireBean(jobInstance);
		return jobInstance;
	}
}