package soya.framework.pipeline.scheduler;

import com.google.common.io.Files;
import com.google.gson.*;
import org.quartz.*;
import org.quartz.simpl.SimpleClassLoadHelper;
import org.quartz.xml.XMLSchedulingDataProcessor;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScheduleEventFactory {

    private static ScheduleEventFactory instance;

    private Gson gson;

    static {
        instance = new ScheduleEventFactory();
    }

    private ScheduleEventFactory() {
        this.gson = new GsonBuilder().registerTypeAdapter(Class.class, new JsonDeserializer<Class>() {
            @Override
            public Class deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                try {
                    return Class.forName(jsonElement.getAsString());
                } catch (ClassNotFoundException e) {
                    throw new JsonParseException(e);
                }
            }
        }).create();
    }

    public static ScheduleEventFactory getInstance() {
        return instance;
    }

    public  List<ScheduleEvent> fromFile(File file) throws IOException {
        String extension = Files.getFileExtension(file.getName());
        InputStream inputStream = new FileInputStream(file);
        if ("xml".equalsIgnoreCase(extension)) {
            return fromXml(inputStream);

        } else if ("yaml".equalsIgnoreCase(extension) || "yml".equalsIgnoreCase(extension)) {
            return fromJson(inputStream);

        } else if ("json".equalsIgnoreCase(extension)) {
            return fromJson(inputStream);

        } else {

        }
        return null;
    }

    public  List<ScheduleEvent> fromXml(InputStream inputStream) {
        return null;
    }

    public  List<ScheduleEvent> fromJson(InputStream inputStream) {
        List<ScheduleEvent> list = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(inputStream));
        if (jsonElement.isJsonObject()) {
            list.add(fromJsonObject(jsonElement.getAsJsonObject()));

        } else if (jsonElement.isJsonArray()) {
            jsonElement.getAsJsonArray().forEach(e -> {
                list.add(fromJsonObject(e.getAsJsonObject()));
            });
        }
        return list;
    }

    public  List<ScheduleEvent> fromJson(Reader reader) {
        List<ScheduleEvent> list = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseReader(reader);
        if (jsonElement.isJsonObject()) {
            list.add(fromJsonObject(jsonElement.getAsJsonObject()));

        } else if (jsonElement.isJsonArray()) {
            jsonElement.getAsJsonArray().forEach(e -> {
                list.add(fromJsonObject(e.getAsJsonObject()));
            });
        }
        return list;
    }

    private ScheduleEvent fromJsonObject(JsonObject jsonObject) {

        Schedule schedule = gson.fromJson(jsonObject, Schedule.class);

        JobDetail jobDetail = JobBuilder.newJob(schedule.jobClass)
                .withIdentity(schedule.name, schedule.group)
                .withDescription(schedule.description)
                .build();

        List<Trigger> triggers = new ArrayList<>();
        for(TriggerConfig tc: schedule.triggers) {
            TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(tc.name, tc.group)
                    .withPriority(tc.priority)
                    .startAt(tc.startTime)
                    .endAt(tc.endTime);

            if (tc.cron != null) {
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(tc.cron));

            } else if (tc.interval != null) {
                if (tc.count == null) {

                } else {
                    switch (tc.unit) {
                        case DAYS:
                            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24 * tc.interval));
                            break;
                        case HOURS:
                            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(tc.interval));
                            break;
                        case MINUTES:
                            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(tc.interval));
                            break;
                        case SECONDS:
                            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(tc.interval));
                            break;
                        default:
                            throw new IllegalStateException("TimeUnit '" + tc.unit + "' is not supported.");
                    }
                }

            }

            triggers.add(triggerBuilder.build());
        }

        return new ScheduleEvent(jobDetail, triggers);
    }

    public static List<ScheduleEvent> fromYaml(InputStream inputStream) {
        return null;
    }

    static class XMLParser extends XMLSchedulingDataProcessor {
        public XMLParser() throws ParserConfigurationException {
            super(new SimpleClassLoadHelper());
        }
    }

    static class Schedule {
        private String group;
        private String name;
        private Class<? extends Job> jobClass;
        private String description;

        private TriggerConfig[] triggers;

    }

    static class TriggerConfig {
        private String group;
        private String name;
        private String description;
        private int priority = 3;

        private Date startTime;
        private Date endTime;

        private Integer count;
        private Integer interval;
        private TimeUnit unit = TimeUnit.SECONDS;

        private String cron;
    }
}
