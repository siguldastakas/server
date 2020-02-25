<#include '../template.ftl'>
<@html>
    <section>
        <h1>${event.name}</h1>
        <#if saved>
            <p>Rezultāti ir saglabāti!</p>
        <#else>
            <p class="warning">Rezultāti nav saglabāti!</p>
        </#if>
    </section>
    <#if lofXml??>
    <section>
        <a href="${lofXml}">LOF XML</a>
    </section>
    </#if>
    <section>
        <form action="${upload}" method="post" enctype="multipart/form-data">
            <label>Augšupielādēt rezultātus:</label>
            <input type="file" name="xml">
            <input type="submit" value="Augšupielādēt" style="float: right">
        </form>
    </section>
</@>