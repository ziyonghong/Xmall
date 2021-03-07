package util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;


/**
 * <p>���ƣ�IdWorker.java</p>
 * <p>�������ֲ�ʽ������ID</p>
 * <pre>
 *     Twitter�� Snowflake��JAVAʵ�ַ���
 * </pre>
 * ���Ĵ���Ϊ��IdWorker�����ʵ�֣���ԭ��ṹ���£��ҷֱ���һ��0��ʾһλ���á��ָ���ֵ����ã�
 * 1||0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
 * ��������ַ����У���һλΪδʹ�ã�ʵ����Ҳ����Ϊlong�ķ���λ������������41λΪ���뼶ʱ�䣬
 * Ȼ��5λdatacenter��ʶλ��5λ����ID���������ʶ����ʵ����Ϊ�̱߳�ʶ����
 * Ȼ��12λ�ú����ڵĵ�ǰ�����ڵļ������������պ�64λ��Ϊһ��Long�͡�
 * �����ĺô��ǣ������ϰ���ʱ���������򣬲��������ֲ�ʽϵͳ�ڲ������ID��ײ����datacenter�ͻ���ID�����֣���
 * ����Ч�ʽϸߣ������ԣ�snowflakeÿ���ܹ�����26��ID���ң���ȫ������Ҫ��
 * <p>
 * 64λID (42(����)+5(����ID)+5(ҵ�����)+12(�ظ��ۼ�))
 *
 * @author Polim
 */
public class IdWorker {
    // ʱ����ʼ��ǵ㣬��Ϊ��׼��һ��ȡϵͳ�����ʱ�䣨һ��ȷ�����ܱ䶯��
    private final static long twepoch = 1288834974657L;
    // ������ʶλ��
    private final static long workerIdBits = 5L;
    // �������ı�ʶλ��
    private final static long datacenterIdBits = 5L;
    // ����ID���ֵ
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // ��������ID���ֵ
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // ����������λ
    private final static long sequenceBits = 12L;
    // ����IDƫ����12λ
    private final static long workerIdShift = sequenceBits;
    // ��������ID����17λ
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    // ʱ���������22λ
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);
    /* �ϴ�����idʱ��� */
    private static long lastTimestamp = -1L;
    // 0����������
    private long sequence = 0L;

    private final long workerId;
    // ���ݱ�ʶid����
    private final long datacenterId;

    public IdWorker(){
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
    }
    /**
     * @param workerId
     *            ��������ID
     * @param datacenterId
     *            ���к�
     */
    public IdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }
    /**
     * ��ȡ��һ��ID
     *
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            // ��ǰ�����ڣ���+1
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // ��ǰ�����ڼ������ˣ���ȴ���һ��
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        // IDƫ������������յ�ID��������ID
        long nextId = ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * <p>
     * ��ȡ maxWorkerId
     * </p>
     */
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
         /*
          * GET jvmPid
          */
            mpid.append(name.split("@")[0]);
        }
      /*
       * MAC + PID �� hashcode ��ȡ16����λ
       */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * <p>
     * ���ݱ�ʶid����
     * </p>
     */
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }

    
    public static void main(String[] args) {
		
    	IdWorker idWorker=new IdWorker(0,0);
    	
    	for(int i=0;i<100;i++){
    		long nextId = idWorker.nextId();
        	System.out.println(nextId);
    	}
    	
    	
	}

}