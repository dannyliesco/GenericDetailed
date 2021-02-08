import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Person{
    void work(){
        System.out.println("Everybody needs to work...");
    }
}

class Worker extends Person{
    String workername = "Worker";
    @Override
    void work() {
        System.out.println("Worker need work hard to survie !");
    }
}

class Programmer extends Worker{
    @Override
    void work() {
        System.out.println("Progammer work is coding...");
    }
    void onlyProgrammerCanDo(){
        System.out.println("Sth that only programmer can do");
    }
}

class JavaProgrammer extends Programmer{
    @Override
    void work() {
        System.out.println("JavaProgrammer use java to coding...");
    }
}

class JavaArchitectProgrammer extends JavaProgrammer{
    @Override
    void work() {
        System.out.println("JavaArchitect needn't to code , but they don't have hair...");
    }
}

public class Generic {
    public static void main(String[] args) {
        JavaProgrammer javaProgrammer = new JavaProgrammer();
        JavaArchitectProgrammer architectProgrammer = new JavaArchitectProgrammer();

        //子类Worker转换成父类型Person（向上转型）
        Person person = new Worker();
        System.out.println("向上转型结果(upcastring result): ");
        person.work();
        //但是向上转型会存在数据丢失
        //无法获取到person.workername

        //向下转型（有风险需谨慎）会导致运行时异常
        Programmer programmer = new JavaArchitectProgrammer();
        JavaArchitectProgrammer javaArchitectProgrammer = (JavaArchitectProgrammer) programmer;
        System.out.println("向下转型结果(downcasting result): ");
        programmer.work();
        //父类引用指向子类对象,不是打印Progammer work is coding...,因为发生了动态绑定.
        javaArchitectProgrammer.work();
        //下面代码报错!
        JavaArchitectProgrammer javaArchitectProgrammer1 = (JavaArchitectProgrammer) new Programmer();
        /**
         * 1.总结:
         * 父类引用指向子类对象，而子类引用不能指向父类对象
         * 把子类对象直接赋给父类引用叫upcasting向上转型，向上转型不用强制转换
         * 把指向子类对象的父类引用赋给子类引用叫向下转型(downcasting)，要强制转换
         * 2.为什么需要转型:
         * 父类想要访问子类特有的方法时，需要使用向下转型
         * 抽象用到向上转型
         * Next -> 上下边界
         */
        Worker worker = new Worker();



        /**
         * 上边界通配符（<? extends 父类型>）
         * 下边界通配符 (<? super 子类型>)
         */
        List<? extends Programmer> programmerUpperList = null;
        //源码List<E>中的E是泛型, ? extend T代表一种实际类型了, 跟Interger,String一样
        //赋值
        programmerUpperList = Arrays.asList(
                new Programmer(),
                new JavaProgrammer(),
                new JavaArchitectProgrammer()
        );
        //因为唯一可以确定此列表的元素类型的父类型是Programmer，所以可以将取得的元素值赋值给Programmer对象
        programmer = programmerUpperList.get(0);
        //Worker和Person是其父类，可以再次向上转型
        worker = programmerUpperList.get(1);
        person = programmerUpperList.get(2);

        //虽然此列表被实例化成JavaProgrammer的列表，但是不能确定其中的数据就是JavaProgrammer类型的，所以不能将取得的值赋值给JavaProgrammer对象
        programmerUpperList.add(javaArchitectProgrammer);


        /**上边界通配符（<? extends 父类型>）
         * programmerUppoerList 成员变量 T extends Programmer t,一旦实例化就确定下来了
         * 类构造的时候this.t未知
         * 在获取数据时 [ 返回类型 get(){ return this.t;}
         * 虽然不能确定返回 this.t的是何种类型，但是this.t的类型肯定是返回类型的子类型
         * 外部引用获取的值是通过向上转型,允许的

         * 在写入数据时 [ void set(参数类型 m){ this.t = m;}]
         * 而传入的类型 m 可能是this.t的子类, 所以此时set方法进行的是向下转型(this.t = t,子类引用指向父类对象,存在风险).
         * Java泛型为了减低类型转换的安全隐患，直接就不允许这种操作了
         */

        //测试下边界类型通配符
        //定义一个列表，唯一可以确定的是：此列表的元素类的子类型是Programmer
        List<? super Programmer> programmerLowerList = null;
        //do something ...
        //某种情形下，此列表被赋值成Worker的列表
        programmerLowerList = new ArrayList<Worker>();
        //因为无法确定对象的实例化类型是Programmer、Worker还是Person，所有不能get
        programmer = programmerLowerList.get(0);
        worker = programmerLowerList.get(0);
        //因为唯一可以确定此列表的元素类型的子类是Programmer，所以可以添加Programmer类型的对象及其子类型
        programmerLowerList.add(programmer);
        programmerLowerList.add(javaProgrammer);
        programmerLowerList.add(architectProgrammer);

        /**下边界通配符 参数类型 =（<? super 子类型>）
         在获取数据时 [ 返回类型 get(){ return this.t;} ]：
         类构造的时候this.t = m(是t的父类) 发生了向下转型
         可以确定参数的类型是某个子类型，所以可以将返回类型设置为子类型
         不能确定返回的是何种类型，但是返回的对象this.t的类型肯定是返回类型的父类型，外部要获取的引用类型也是返回类型的父类型,
         在不是Object的情况下,此时进行的是向下转型。
         Java泛型为了减低类型转换的安全隐患，不允许这种操作。

         在写入数据时 [ void set(参数类型<? super 子类型> t){ this.t = t;} ]
         传入的类型可能是t的父类, 所以此时set方法进行的是向上转型
         虽然不能确定this.t字段的具体类型，但是肯定是参数类型的父类型，所以此时set方法进行的肯定是向上转型，写入成功。
         */
    }
}

