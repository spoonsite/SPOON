

<div style="height:100px;overflow:auto;">

	<tpl if="one && one.length &gt; 0">
		<div>
			<h3 class="quickView toggle-collapse">Entry one <div data-qtip="Expand panel" style="float: right;" data-ref="toolEl" class=" x-tool-tool-el x-tool-img x-tool-expand-bottom eval-toggle-caret" role="presentation"></div></h3>
			<section class="eval-visible-false">
				<table class="quickView-table" border="1">
					<tr>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Label</th>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Value</th>
						<th class="quickView-tableheader quickView-tableall quickView-table-padding">Unit</th>
					</tr>
					<tpl for="one">
						<tr class="quickView-table">
							<td class="quickView-tableall" style="width: 30%;"><b>{label}</b>
								<tpl if="privateFlag"> <span class="private-badge">private</span></tpl>
							</td>
							<td class="quickView-tableall">
								<b>{value}</b>
								<tpl if="comment"><hr>Comment: {comment}</tpl>
							</td>
							<td class="quickView-tableall">
								<tpl if="unit">
									<b>{unit}</b>
								</tpl>
							</td>
						</tr>
					</tpl>
				</table>
			</section>
		</div>
	</tpl>
   
   </div>