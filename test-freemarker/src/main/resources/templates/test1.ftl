<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}
<br>
<hr>
遍历数据模型中的list学生信息（数据模型中的名称为stus）:<br>
<table>
    <tr>
        <th>序号</th>
        <th>姓名</th>
        <th>年龄</th>
        <th>金额</th>
        <th>出生日期</th>
    </tr>
    <#list stus as stu>
        <tr>
            <td>${stu_index}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            <td>${stu.money}</td>
            <#--<td>${stu.birthday}</td>-->
        </tr>
    </#list>
</table>
<br>
<hr>
获取数据模型中的stuMap的值：<br>
姓名：${stuMap['stu1'].name}<br>
年龄：${stuMap['stu1'].age}<br>
金额：${stuMap.stu1.money}<br>

遍历Map中的key：<br>
<#list stuMap?keys as k>
    <td>${stuMap[k].name}</td>
    <td>${stuMap[k].age}</td>
    <td>${stuMap[k].money}</td><br>
</#list>

<br>
<hr>
if指令：<br>
<#list stus as stu>
    <div <#if stu.name == '小明'>style="color: pink" </#if> >${stu.name}</div>
    <div>${stu.age}</div>
    <div <#if (stu.money gt 300)>style="color: red" </#if>>${stu.money}</div>
</#list>

<br>
<hr>
空值处理：<br>
方法1：
<#if stus??>
    <#list stus as stu>
        <div>${stu.name}</div>
        <div>${stu.age}</div>
    </#list>
</#if>
方法2：
<#list stus as stu>
    <div>${(stu.name)!''}</div>
    <div>${(stu.age)!''}</div>
</#list>

<hr>
获取某个集合的大小：<br>
学生的人数：${stus?size}

<br>
<hr>
日期格式化：<br>
显示年月日: ${stu1.birthday?date}
显示时分秒：${stu1.birthday?time}
显示日期+时间：${stu1.birthday?datetime}
自定义格式化： ${stu1.birthday?string("yyyy年MM月")}

</body>
</html>
