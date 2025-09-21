package basics.mastery.tools;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * ThreadDiagnosticsQuickstart
 *
 * Beginner-friendly quickstart showing how to capture a simple thread dump
 * and detect deadlocks programmatically using ThreadMXBean.
 *
 * Additive educational utility; does not modify existing logic.
 */
public class ThreadDiagnosticsQuickstart {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread Diagnostics Quickstart ===\n");
        printCompactThreadDump(5);
        detectDeadlocks();
    }

    public static void printCompactThreadDump(int limit) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] infos = bean.dumpAllThreads(true, true);
        System.out.println("Top " + Math.min(limit, infos.length) + " threads:");
        for (int i = 0; i < Math.min(limit, infos.length); i++) {
            ThreadInfo ti = infos[i];
            System.out.printf("  #%d %s state=%s lock=%s owner=%s%n",
                ti.getThreadId(), ti.getThreadName(), ti.getThreadState(),
                ti.getLockName(), ti.getLockOwnerName());
        }
    }

    public static void detectDeadlocks() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] ids = bean.findDeadlockedThreads();
        if (ids == null) {
            System.out.println("No deadlocks detected.");
        } else {
            System.out.println("Deadlocks detected in thread IDs: " + java.util.Arrays.toString(ids));
        }
    }
}
