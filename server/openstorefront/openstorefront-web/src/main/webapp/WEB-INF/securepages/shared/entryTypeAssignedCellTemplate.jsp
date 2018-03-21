<style>
	.cell {
		min-height: 2em;
		text-align: center;
	}
	.sub-cell-status {
		width: 40%;
		float: left;
	}
	.sub-cell-records {
		width: 60%;
		float: right;
	}
	.inherit-symbol {
		padding: 2px;
		border-radius: 10px;
		background: #777;
		color: #fff;
	}
	.cell-button {
		width: 100%;
		border: 1px solid rgb(210,210,210);
		padding: 4px;
		margin: 3px;
		background: rgb(230,230,230); 
		border-radius: 2px;
	}
	.cell-button:hover {
		background: #ccc;
		cursor: pointer;
	}
</style>

<tpl if="isAssigned">
	<div class="sub-cell-status cell">
		<tpl if="cameFromAncestor">
			<div>
				<i class="fa fa-sitemap inherit-symbol" 
					style="margin-top: <tpl if='data.length &gt; 1'>1<tpl else>0</tpl>em;"
					data-qtip="<tpl if="type=='roles'">Group(s)<tpl else>User(s)</tpl> inherited from '<b>{rootNode.data.componentType.componentType}</b>'"
				></i>
			</div>
		</tpl>
		<tpl if="!cameFromAncestor">
			<div>
				<button class="cell-button" style="margin-top: <tpl if='data.length &gt; 1 && !cameFromAncestor'>1<tpl else>0</tpl>em;" onclick="
					(function (data, type) {
						data = data.split(',');
						var windowConfig = {
							modal: true,
							minWidth: 200,
							title: type === 'roles' ? 'Assigned Groups' : 'Assigned Users'
						};

						if (type === 'roles') {
							Ext.Array.forEach(data, function (el, index) {
								data[index] = {html: el, xtype: 'container', padding: 15};
							});
							Ext.create('Ext.window.Window', Ext.apply({items: data}, windowConfig)).show();
						}
						else {

							Ext.Ajax.request({
								url: 'api/v1/resource/userprofiles/lookup',
								success: function (response) {

									var allUsers = Ext.decode(response.responseText);
									for (var i = 0; i < allUsers.length; i += 1) {
										for (var j = 0; j < data.length; j += 1) {

											if (allUsers[i].code === data[j]) {
												data[j] = {html: allUsers[i].description, padding: 15};
											}
										}
									}

									Ext.create('Ext.window.Window', Ext.apply({items: data}, windowConfig)).show();
								}
							})
						}
					}('{data}', '{type}'));
				">
					<tpl if="type == 'roles'">
						View Groups
					<tpl else>
						View Users
					</tpl>
				</button>
			</div>
		</tpl>
	</div>
	<div class="sub-cell-records cell">
		<tpl for="data">
			<div>
				<tpl if="xindex &lt; 3">
					{.}
				</tpl>
				<tpl if="xindex == 3">
					&#8942;
				</tpl>
			</div>
		</tpl>
	</div>
<tpl else>
	<tpl for="data">
		<div>{.}</div>
	</tpl>
</tpl>

