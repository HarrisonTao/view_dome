package com.dykj.youfeng.mode.receipt;

/**
 * <pre>
 *           .----.
 *        _.'__    `.
 *    .--(Q)(OK)---/$\
 *  .' @          /$$$\
 *  :         ,   $$$$$
 *   `-..__.-' _.-\$$/
 *         `;_:    `"'
 *       .'"""""`.
 *      /,  oo  ,\
 *     //         \\
 *     `-._______.-'
 *     ___`. | .'___
 *    (______|______)
 * </pre>
 * <p>文件描述：<p>
 * <p>作者：lj<p>
 * <p>创建时间：2020/3/14<p>
 * <p>更改时间：2020/3/14<p>
 * <p>版本号：1<p>
 */
public class JftApplyPaySmsBean {
    /**
     * status : 8888
     * message : {"chSerialNo":"20191120174933908876","tranStatus":"3",
     * "orderNo":"JftM20191120174933823"}
     * {
     *     "status":"9009",
     *     "message":"系统繁忙稍后重试!"
     * }
     */

    private String status;
    private Object message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
