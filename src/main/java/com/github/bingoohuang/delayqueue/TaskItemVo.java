package com.github.bingoohuang.delayqueue;

import com.github.bingoohuang.utils.cron.CronAlias;
import com.github.bingoohuang.utils.cron.CronExpression;
import com.github.bingoohuang.westcache.utils.FastJsons;
import com.github.bingoohuang.westid.WestId;
import com.google.common.base.MoreObjects;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Value
@Builder
public class TaskItemVo {
  private final String taskId; // 可选：任务ID
  private final String relativeId; // 可选：关联ID，例如订单ID，卡ID，课程ID，审批流程ID等。
  private final String classifier; // 可选：任务分类名称，例如子系统名称。
  private final String taskName; // 必须：任务名称
  private final String taskService; // 必须(2选1)：任务执行服务名称
  private final Class<?> taskServiceClass; // 必须(2选1)：任务执行服务类名
  private final DateTime runAt; // 可选：可以开始运行的时间，设定在将来，获得延时执行
  private final int timeout; // 可选：任务超时秒数
  private final Object attachment; // 可选：任务附件（必须可JSON化）
  private final String var1;
  private final String var2;
  private final String var3; // 可选：参数
  private final String scheduled; // 可选：排期
  private final String resultStore; // 可选：任务结果存储，默认DIRECT

  public TaskItem createTaskItem(long versionNumber) {
    String taskServiceName = taskService;
    if (StringUtils.isEmpty(taskServiceName) && taskServiceClass != null) {
      taskServiceName = TaskUtil.getSpringBeanDefaultName(taskServiceClass);
    }

    CronExpression cron = null;
    if (StringUtils.isNotEmpty(scheduled)) {
      cron = CronAlias.create(scheduled); // ensure that scheduled expression is valid
    }

    return TaskItem.builder()
        .taskId(MoreObjects.firstNonNull(getTaskId(), String.valueOf(WestId.next())))
        .relativeId(getRelativeId())
        .classifier(MoreObjects.firstNonNull(getClassifier(), "default"))
        .taskService(checkNotEmpty(taskServiceName, "任务执行服务名称不可缺少"))
        .taskName(MoreObjects.firstNonNull(getTaskName(), taskServiceName))
        .state(TaskItem.待运行)
        .runAt(TaskUtil.emptyThenNow(getRunAt(), cron))
        .timeout(getTimeout())
        .attachment(FastJsons.json(getAttachment()))
        .var1(getVar1())
        .var2(getVar2())
        .var3(getVar3())
        .scheduled(scheduled)
        .resultStore(
            MoreObjects.firstNonNull(getResultStore(), DirectResultStore.class.getSimpleName()))
        .createTime(DateTime.now())
        .versionNumber(versionNumber)
        .build();
  }

  private String checkNotEmpty(String str, String desc) {
    if (isNotEmpty(str)) return str;

    throw new RuntimeException(desc);
  }
}
