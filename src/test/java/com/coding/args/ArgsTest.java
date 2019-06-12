package com.coding.args;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ArgsTest {

    /**
     * 1.用测试用例框定需求范围,写一个测试用例,包括核心的节点的几个事情,然后 Igore 掉
     * 2.把字符串转换为 schema 对象,schema l:boolean p:integer d:string 的转换,可以获取 size,可以获取defaultValue,可以获取 type;
     * 3.测试schema 参数与结构不匹配,返回匹配的异常信息
     * 4.测试解析流程 单个字符串和单个 schema 的解析, 解析完可以通过 args.getValue("l") 获取
     * 5.定义 parse() 解析函数,函数通过解析字符串 -l true ,得到value->true ,并根据 falg 获取类型,转换为正确的类型
     * 6.parse(),测试 -l 无参数的情况,ags.getValue("l"),args.getValue("p'),args.getValue("d")
     * 7.测试多个命令字符串,多个 schema 的情况
     * 8.扩展需求,测试新类型string[],integer[] 的情况
     */

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
//    @Ignore
    public void should_test_demo() {

        String argsText = "-l -p 8080 -d /usr/log";

        SchemaDesc schemaDesc = new SchemaDesc("l:boolean p:integer d:string");

        Args args = new Args(argsText, schemaDesc);
        assertThat(args.getValue("l"), is(false));
        assertThat(args.getValue("p"), is(8080));
        assertThat(args.getValue("d"), is("/usr/log"));
    }

    @Test
    public void should_parse_schema_text() {
        SchemaDesc schemaDesc = new SchemaDesc("l:boolean p:integer d:string");
        assertThat(schemaDesc.getSize(), is(3));
        assertThat(schemaDesc.getDefaultValue("l"), is(false));
        assertThat(schemaDesc.getType("l"), is("boolean"));

        assertThat(schemaDesc.getDefaultValue("p"), is(0));
        assertThat(schemaDesc.getType("p"), is("integer"));

        assertThat(schemaDesc.getDefaultValue("d"), is(""));
        assertThat(schemaDesc.getType("d"), is("string"));
    }

    @Test
    public void should_throw_illegalArgumentException() {
        SchemaDesc schemaDesc = new SchemaDesc("b:byte");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("This byte type is not supported");
        assertThat(schemaDesc.getDefaultValue("b"), is("byte"));
    }

    @Test
    public void should_parse_one_cmdstr_and_one_schema() {
        SchemaDesc schemaDesc = new SchemaDesc("l:boolean");
        Args args = new Args("-l true", schemaDesc);
        assertThat(args.getValue("l"), is(true));

        schemaDesc = new SchemaDesc("p:integer");
        args = new Args("-p 8080", schemaDesc);
        assertThat(args.getValue("p"), is(8080));

        schemaDesc = new SchemaDesc("d:string");
        args = new Args("-d /usr/log", schemaDesc);
        assertThat(args.getValue("d"), is("/usr/log"));
    }

    @Test
    public void should_return_defalutValue_by_single_flag() {

        SchemaDesc schemaDesc = new SchemaDesc("l:boolean");
        Args args = new Args("-l", schemaDesc);
        assertThat(args.getValue("l"), is(false));

        schemaDesc = new SchemaDesc("p:integer");
        args = new Args("-p", schemaDesc);
        assertThat(args.getValue("p"), is(0));

        schemaDesc = new SchemaDesc("d:string");
        args = new Args("-d", schemaDesc);
        assertThat(args.getValue("d"), is(""));
    }

    @Test
    public void should_parse_args_value_array() {

        String argsText = "-l -p 8080 -d /usr/log -s this,is,all -w 90,97,98";

        SchemaDesc schemaDesc = new SchemaDesc("l:boolean p:integer d:string s:string[] w:integer[]");

        Args args = new Args(argsText, schemaDesc);
        assertThat(args.getValue("l"), is(false));
        assertThat(args.getValue("p"), is(8080));
        assertThat(args.getValue("d"), is("/usr/log"));
        assertThat(args.getValue("s"), is(new String[]{"this", "is", "all"}));
        assertThat(args.getValue("w"), is(new Integer[]{90, 97, 98}));

    }
}