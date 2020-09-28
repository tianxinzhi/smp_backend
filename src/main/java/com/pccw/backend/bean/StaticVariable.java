package com.pccw.backend.bean;

/**
 * common table field value
 */
public class StaticVariable {

    /**
     * table => BaseLog
     */
    public static final String LOGTYPE_ORDER = "O";
    public static final String LOGTYPE_MANAGEMENT="M";
    public static final String LOGTYPE_REPL="R";

    public static final String LOGORDERNATURE_ASSIGN = "Assign(ASG)";
    public static final String LOGORDERNATURE_RETURN = "Return(RET)";
    public static final String LOGORDERNATURE_EXCHANGE = "Exchange(EXC)";
    public static final String LOGORDERNATURE_ADVANCED_RESERVE = "Advanced Reserve(ARS)";
    public static final String LOGORDERNATURE_CANCEL_ADVANCE_RESERVE = "Cancel Advance Reserve(CARS)";
    public static final String LOGORDERNATURE_ADVANCE_PICK_UP = "Advance Pick Up(APU)";
    public static final String LOGORDERNATURE_REPLENISHMENT_REQUEST = "Replenishment Request(RREQ)";
    public static final String LOGORDERNATURE_REPLENISHMENT_RECEIVEN = "Replenishment Recvice(RREC)";
    public static final String LOGORDERNATURE_STOCK_TAKE_ADJUSTMENT = "Stock Take Adjustment(STA)";
    public static final String LOGORDERNATURE_STOCK_TRANSFER_OUT = "Stock Transfer Out(TRO)";
    public static final String LOGORDERNATURE_STOCK_OUT_STS = "Stock Out To Shop(SOTS)";
    public static final String LOGORDERNATURE_STOCK_OUT_STW = "Stock Out To Warehouse(SOTW)";
    public static final String LOGORDERNATURE_STOCK_TRANSFER_IN= "Stock Transfer In(TRI)";
    public static final String LOGORDERNATURE_STOCK_IN_STS = "Stock In From Shop(SIFS)";
    public static final String LOGORDERNATURE_STOCK_IN_FROM_WAREHOUSE = "Stock In From Warehouse(SIFW)";
    public static final String LOGORDERNATURE_STOCK_IN_WITHOUT_PO_STW = "Stock In Without PO(SIWPO)";
    public static final String LOGORDERNATURE_STOCK_RESERVE = "Stock Reserve(RES)";
    public static final String LOGORDERNATURE_STOCK_CANCEL_RESERVE = "Stock Cancel Reserve(CRES)";
    public static final String LOGORDERNATURE_TRANSFER_TO_WAREHOUSE = "Stock Transfer Warehouse(TRW)";
    public static final String LOGORDERNATURE_STOCK_THRESHOLD = "Stock Threshold(STHR)";
    public static final String LOGORDERNATURE_STOCK_CATEGORY = "Stock Change Category(SCC)";
    public static final String LOGORDERNATURE_STOCK_TAKE = "Stock Take(ST)";

    
    public static final String STATUS_WAITING = "W";
    public static final String STATUS_DONE = "D";


    /**
     * table => BaseLogDtl
     */
    public static final String DTLACTION_ADD = "A";
    public static final String DTLACTION_DEDUCT="D";

    public static final String DTLSUBIN_GOOD = "Good";
    public static final String DTLSUBIN_AVAILABLE = "Available";
    public static final String DTLSUBIN_FAULTY = "Faulty";
    public static final String DTLSUBIN_INTRANSIT = "Intransit";
    public static final String DTLSUBIN_RESERVED = "Reserved";
    public static final String DTLSUBIN_RESERVED_WITH_AO = "Reserved With AO";
    public static final String DTLSUBIN_DEMO = "Demo";
    public static final String DTLSUBIN_RESERVED_WITH_REMOTE = "Reserved With Remote";

    public static final String LISSTATUS_WAITING = "W";
    public static final String LISSTATUS_DONE = "D";

    public static final String STATUS_AVAILABLE = "AVL";
    public static final String STATUS_FAULTY = "FAU";
    public static final String STATUS_INTRANSIT = "DEL";
    public static final String STATUS_RESERVED = "RES";
    public static final String STATUS_RESERVED_WITH_AO = "RAO";
    public static final String STATUS_DEMO = "DEM";
    public static final String STATUS_RESERVED_WITH_REMOTE = "RRO";



    public static final String SKU_ORIGIN_FROM_WITHPO = "1";
    public static final String SKU_ORIGIN_FROM_LIS = "2";
    public static final String SKU_ORIGIN_FROM_OTHER = "3";


    /**
     * table => Process
     */
    public static final String PROCESS_APPROVED_STATUS = "APPROVED";
    public static final String PROCESS_REJECTED_STATUS = "REJECTED";
    public static final String PROCESS_WAITING_STATUS = "WAITING";
    public static final String PROCESS_PENDING_STATUS = "PENDING";






}
