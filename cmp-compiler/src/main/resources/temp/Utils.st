package <bean.packageName>;

public final class Utils(){
    private Utils(){}

<bean.checkMethods:{method|
    public final static boolean check(<method.type> type){
        if(<method.type><method.trueCondition>)
            return true;
        else
            return false;
    }
}>
}