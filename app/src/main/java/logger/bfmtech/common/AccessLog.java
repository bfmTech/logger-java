package logger.bfmtech.common;

/*
 *  AccessLog 日志格式参数
 */
public class AccessLog {
   private String method;
   private int status;
   private long begintime;
   private long endtime;
   private String referer;
   private String httphost;
   private String interfac;
   private String reqquery;
   private String reqbody;
   private String resbody;
   private String clientip;
   private String useragent;
   private String headers;
   private String reqId;

   public AccessLog(String method, int status, long begintime, long endtime, String referer, String httphost,
         String interfac, String reqquery, String reqbody, String resbody, String clientip, String useragent,
         String headers, String reqId) {
      this.method = method;
      this.status = status;
      this.begintime = begintime;
      this.endtime = endtime;
      this.referer = referer;
      this.httphost = httphost;
      this.interfac = interfac;
      this.reqquery = reqquery;
      this.reqbody = reqbody;
      this.resbody = resbody;
      this.clientip = clientip;
      this.useragent = useragent;
      this.headers = headers;
      this.reqId = reqId;
   }

   public String getMethod() {
      return method;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public int getStatus() {
      return status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public long getBegintime() {
      return begintime;
   }

   public void setBegintime(long begintime) {
      this.begintime = begintime;
   }

   public long getEndtime() {
      return endtime;
   }

   public void setEndtime(long endtime) {
      this.endtime = endtime;
   }

   public String getReferer() {
      return referer;
   }

   public void setReferer(String referer) {
      this.referer = referer;
   }

   public String getHttphost() {
      return httphost;
   }

   public void setHttphost(String httphost) {
      this.httphost = httphost;
   }

   public String getInterfac() {
      return interfac;
   }

   public void setInterfac(String interfac) {
      this.interfac = interfac;
   }

   public String getReqquery() {
      return reqquery;
   }

   public void setReqquery(String reqquery) {
      this.reqquery = reqquery;
   }

   public String getReqbody() {
      return reqbody;
   }

   public void setReqbody(String reqbody) {
      this.reqbody = reqbody;
   }

   public String getResbody() {
      return resbody;
   }

   public void setResbody(String resbody) {
      this.resbody = resbody;
   }

   public String getClientip() {
      return clientip;
   }

   public void setClientip(String clientip) {
      this.clientip = clientip;
   }

   public String getUseragent() {
      return useragent;
   }

   public void setUseragent(String useragent) {
      this.useragent = useragent;
   }

   public String getHeaders() {
      return headers;
   }

   public void setHeaders(String headers) {
      this.headers = headers;
   }

   public String getReqId() {
      return reqId;
   }

   public void setReqId(String reqId) {
      this.reqId = reqId;
   }

   @Override
   public String toString() {
      return String.join(Consts.Separator, method, String.valueOf(status), String.valueOf(begintime),
            String.valueOf(endtime), referer, httphost, interfac, reqquery, reqbody, resbody, clientip, useragent,
            reqId,headers);
   }
}
