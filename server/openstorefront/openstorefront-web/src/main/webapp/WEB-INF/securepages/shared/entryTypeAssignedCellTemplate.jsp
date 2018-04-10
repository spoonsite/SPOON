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
				<tpl if='data.length &gt; 1'>
				<i class="fa fa-sitemap inherit-symbol" 
					style="margin-top: 1em;"
					data-qtip="<tpl if="type=='roles'">Group(s)<tpl else>User(s)</tpl> inherited from '<b>{rootNode.data.componentType.componentType}</b>'">
				</i>
				<tpl else>
					<i class="fa fa-sitemap inherit-symbol" 
						style="margin-top: 0em;"
						data-qtip="<tpl if="type=='roles'">Group(s)<tpl else>User(s)</tpl> inherited from '<b>{rootNode.data.componentType.componentType}</b>'">
					</i>
				</tpl>	
			</div>
		</tpl>
		<tpl if="!cameFromAncestor">
			<div>
				<tpl if='data.length &gt; 1 && !cameFromAncestor'>
				<button class="cell-button" style="margin-top: 1em;" onclick="
					window.entryTypeAssignedRender('{data}', '{type}');
				">					
					<tpl if="type == 'roles'">
						View Groups
					<tpl else>
						View Users
					</tpl>
				</button>
				<tpl else>
					<button class="cell-button" style="margin-top: 0em;" onclick="
					window.entryTypeAssignedRender('{data}', '{type}');
					">					
						<tpl if="type == 'roles'">
							View Groups
						<tpl else>
							View Users
						</tpl>
					</button>
				</tpl>	
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

