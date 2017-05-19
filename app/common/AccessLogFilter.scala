package common

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import common.Utils.isAccessLogger

class AccessLogFilter extends Filter[ILoggingEvent] {

  override def decide(event: ILoggingEvent): FilterReply = {
    if (isAccessLogger(event.getLoggerName)) {
      FilterReply.ACCEPT
    } else {
      FilterReply.DENY
    }
  }
}
