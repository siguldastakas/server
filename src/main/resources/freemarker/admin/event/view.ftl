<#include '../template.ftl'>
<@html>
    <section>
        <h1>${event.name}</h1>
    </section>
    <section>
        <form action="${upload}" method="post" enctype="multipart/form-data">
            <label>Augšupielādēt rezultātus:</label>
            <input type="file" name="xml">
            <input type="submit" value="Augšupielādēt" style="float: right">
        </form>
    </section>
</@>