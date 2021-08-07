package jvm;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/*
 第一周作业:
 2.（必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。
 */
public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        // 创建类加载器
        MyClassLoader myClassLoader = new MyClassLoader();
        // 加载类
        Class<?> hello = myClassLoader.findClass("Hello");
        // 创建对象
        Object o = hello.newInstance();
        // 调用hello方法
        Method method = hello.getMethod("hello");
        method.invoke(o);
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 获取输入流
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(name + ".xlass");
        // 读取数据
        try {
            byte[] bytes = new byte[resourceAsStream.available()];
            resourceAsStream.read(bytes);
            // 转换
            byte[] classBytes = decode(bytes);
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (resourceAsStream != null){
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return super.findClass(name);
    }


    // 解码
    private static byte[] decode(byte[] bytes) {
        byte[] bytes1 = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes1[i] = (byte)(255 - bytes[i]);
        }
        return bytes1;
    }
}