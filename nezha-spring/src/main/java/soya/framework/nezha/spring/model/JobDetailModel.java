package soya.framework.nezha.spring.model;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

public class JobDetailModel {
    private String name;
    private String group;
    private String description;
    private String jobClass;
    private boolean durable;
    private boolean recover;

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getDescription() {
        return description;
    }

    public String getJobClass() {
        return jobClass;
    }

    public boolean isDurable() {
        return durable;
    }

    public boolean isRecover() {
        return recover;
    }

    public JobDetail toJobDetail() throws ClassNotFoundException {
        return JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClass))
                .withIdentity(name, group)
                .withDescription(description)
                .requestRecovery(recover)
                .storeDurably(durable)
                .build();
    }
}
