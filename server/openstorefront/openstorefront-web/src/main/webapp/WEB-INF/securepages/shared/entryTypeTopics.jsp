
<style>
.entry-topics-button .button-content-container {
    position: relative;
    width: 100%;
    height: 100px;
}

.entry-topics-row {
    text-align: center;  
    margin: 1px;  
}

.topic-icon-image-container {
    float: left;
}
.topic-icon-image-container img {
    width: 100px;
    height: 100px;
}
.icon-logo-container {
    position: absolute;
    height: 100px;
    font-weight: bold;
    color: #fff;
    width: 100%;
}
</style>

<div style="width: 90%; margin: auto;">
    <tpl for=".">
        <tpl if="xindex%5==0 || xindex==1">
            <tpl if="xindex!=1">
                </div>
            </tpl>
            <div class="entry-topics-row">
        </tpl>
        <div class="entry-topics-button">
            <div class="button-content-container">
                <div class="topic-icon-image-container">
                    <tpl if="componentType.iconUrl">
                        <img src="{componentType.iconUrl}">
                    </tpl>
                </div>
                <div class="icon-logo-container">
                    <div style="display: table; width: 100%; height: 100px;">
                        <div style="display: table-cell; height: 100px; vertical-align: middle;">
                            {componentType.label}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <tpl if="xindex==xcount"></div></tpl>
    </tpl>
</div>
