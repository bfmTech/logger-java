
package logger.bfmtech.common;

public abstract class Logger {

    public void Debug(String message) {
        Log(Level.Debug, message);
    }

    public void Info(String message) {
        Log(Level.Info, message);
    }

    public void Warn(String message) {
        Log(Level.Warn, message);
    }

    public void Error(Exception message) {
        Log(Level.Error, message.getMessage());
    }

    public void Access(AccessLog accessLog) {
        Log(Level.Access, accessLog.toString());
    }
    
    public void SetStoringDays(int days ){

    }

    public abstract void Log(Level level, String... messages);

    public abstract void Close();
}