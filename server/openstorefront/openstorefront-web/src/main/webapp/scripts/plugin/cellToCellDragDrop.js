/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */

/* global Ext, CoreService, CoreUtil */

Ext.define('OSF.plugin.CellToCellDragDrop', {
    extend: 'Ext.plugin.Abstract',
    alias: 'plugin.celltocelldragdrop',
 
    uses: ['Ext.view.DragZone'],
	/** Based on the Ext.js plugin CellDragDrop, but modified to allow for different drop actions /*
 
    /**
     * @cfg {Boolean} enforceType
     * Set to `true` to only allow drops of the same type.
     *
     * Defaults to `false`.
     */
    enforceType: false,
 
    /**
     * @cfg {Boolean} applyEmptyText
     * If `true`, then use the value of {@link #emptyText} to replace the drag record's value after a node drop.
     * Note that, if dropped on a cell of a different type, it will convert the default text according to its own conversion rules.
     *
     * Defaults to `false`.
     */
    applyEmptyText: false,
 
    /**
     * @cfg {Boolean} emptyText
     * If {@link #applyEmptyText} is `true`, then this value as the drag record's value after a node drop.
     *
     * Defaults to an empty string.
     */
    emptyText: '',
 
    /**
     * @cfg {Boolean} dropBackgroundColor
     * The default background color for when a drop is allowed.
     *
     * Defaults to green.
     */
    dropBackgroundColor: 'rgba(47, 255, 40, 0.5)',
 
    /**
     * @cfg {Boolean} noDropBackgroundColor
     * The default background color for when a drop is not allowed.
     *
     * Defaults to red.
     */
    noDropBackgroundColor: 'red',
 
    //<locale> 
    /**
     * @cfg {String} dragText
     * The text to show while dragging.
     *
     * Two placeholders can be used in the text:
     *
     * - `{0}` The number of selected items.
     * - `{1}` 's' when more than 1 items (only useful for English).
     */
    dragText: '{0} selected row{1}',
    //</locale> 
 
    /**
     * @cfg {String} ddGroup
     * A named drag drop group to which this object belongs. If a group is specified, then both the DragZones and
     * DropZone used by this plugin will only interact with other drag drop objects in the same group.
     */
    ddGroup: "GridDD",
 
    /**
     * @cfg {Boolean} enableDrop
     * Set to `false` to disallow the View from accepting drop gestures.
     */
    enableDrop: true,
 
    /**
     * @cfg {Boolean} enableDrag
     * Set to `false` to disallow dragging items from the View.
     */
    enableDrag: true,
 
    /**
     * @cfg {Object/Boolean} containerScroll
     * True to register this container with the Scrollmanager for auto scrolling during drag operations.
     * A {@link Ext.dd.ScrollManager} configuration may also be passed.
     */
    containerScroll: false,

	/**
     * @cfg {Function} onDrop
     * Function to handle a drop
     * A {@link Ext.dd.ScrollManager} configuration may also be passed.
     */
    onDrop: undefined,
	onEnter: undefined,
	onOut: undefined,
    rowFocus: false,
 
    init: function (view) {
        var me = this;
 
        view.on('render', me.onViewRender, me, {
            single: true
        });
    },
 
    destroy: function () {
        var me = this;
 
        me.dragZone = me.dropZone = Ext.destroy(me.dragZone, me.dropZone);
 
        me.callParent();
    },
 
    enable: function () {
        var me = this;
 
        if (me.dragZone) {
            me.dragZone.unlock();
        }
        if (me.dropZone) {
            me.dropZone.unlock();
        }
        me.callParent();
    },
 
    disable: function () {
        var me = this;
 
        if (me.dragZone) {
            me.dragZone.lock();
        }
        if (me.dropZone) {
            me.dropZone.lock();
        }
        me.callParent();
    },
 
    onViewRender: function (view) {
        var me = this,
            scrollEl;
 
        if (me.enableDrag) {
            if (me.containerScroll) {
                scrollEl = view.getEl();
            }
 
            me.dragZone = new Ext.view.DragZone({
                view: view,
                ddGroup: me.dragGroup || me.ddGroup,
                dragText: me.dragText,
                containerScroll: me.containerScroll,
                scrollEl: scrollEl,
                getDragData: function (e) {
                    var view = this.view,
                        item = e.getTarget(view.getItemSelector()),
                        record = view.getRecord(item),
                        cell = e.getTarget(view.getCellSelector()),
                        dragEl, header;
 
                    if (item) {
                        dragEl = document.createElement('div');
                        dragEl.className = 'x-form-text';
                        dragEl.appendChild(document.createTextNode(cell.textContent || cell.innerText));
 
                        header = view.getHeaderByCell(cell);
                        return {
                            event: new Ext.EventObjectImpl(e),
                            ddel: dragEl,
                            item: e.target,
                            columnName: header.dataIndex,
                            record: record
                        };
                    }
                },
 
                onInitDrag: function (x, y) {
                    var self = this,
                        data = self.dragData,
                        view = self.view,
                        selectionModel = view.getSelectionModel(),
                        record = data.record,
                        el = data.ddel;
 
                    // Update the selection to match what would have been selected if the user had 
                    // done a full click on the target node rather than starting a drag from it. 
                    if (!selectionModel.isSelected(record)) {
                        selectionModel.select(record, true);
                    }
 
                    Ext.fly(self.ddel).update(el.textContent || el.innerText);
                    self.proxy.update(self.ddel);
                    self.onStartDrag(x, y);
                    return true;
                }
            });
        }
 
        if (me.enableDrop) {
			// Grab onDrop/onEnter function.
			var onDrop = me.onDrop;
			var onEnter = me.onEnter;
			var onOut = me.onOut;
            me.dropZone = new Ext.dd.DropZone(view.el, {
                view: view,
                ddGroup: me.dropGroup || me.ddGroup,
                containerScroll: true,
 
                getTargetFromEvent: function (e) {
                    var self = this,
                        view = self.view,
                        cell = e.getTarget(view.cellSelector),
                        row, header;
 
                    // Ascertain whether the mousemove is within a grid cell. 
                    if (cell) {
                        row = view.findItemByChild(cell);
                        header = view.getHeaderByCell(cell);
 
                        if (row && header) {
                            return {
                                node: cell,
                                record: view.getRecord(row),
                                columnName: header.dataIndex
                            };
                        }
                    }
                },
 
                // On Node enter, see if it is valid for us to drop the field on that type of column. 
                onNodeEnter: function (target, dd, e, dragData) {
					if (typeof onEnter !== 'undefined') {
						onEnter(target, dd, e, dragData);
					}

                    var self = this,
                        destType = target.record.getField(target.columnName).type.toUpperCase(),
                        sourceType = dragData.record.getField(dragData.columnName).type.toUpperCase();
 
                    delete self.dropOK;
 
                    // Return if no target node or if over the same cell as the source of the drag. 

                    var invalidNodes = dragData.invalidNodes || [];
                    if (!target || target.node === dragData.item.parentNode || invalidNodes.indexOf(target.record) !== -1) {
                        return;
                    }

                    var recordEl = me.rowFocus ? target.node.parentElement : target.node;
 
                    // Check whether the data type of the column being dropped on accepts the 
                    // dragged field type. If so, set dropOK flag, and highlight the target node. 
                    if (me.enforceType && destType !== sourceType) {
 
                        self.dropOK = false;
 
                        if (me.noDropCls) {
                            Ext.fly(recordEl).addCls(me.noDropCls);
                        } else {
                            Ext.fly(recordEl).applyStyles({
                                backgroundColor: me.noDropBackgroundColor
                            });
                        }
 
                        return false;
                    }
 
                    self.dropOK = true;
 
                    if (me.dropCls) {
                        Ext.fly(recordEl).addCls(me.dropCls);
                    } else {
                        Ext.fly(recordEl).applyStyles({
                            backgroundColor: me.dropBackgroundColor
                        });
                    }
                },
 
                // Return the class name to add to the drag proxy. This provides a visual indication 
                // of drop allowed or not allowed. 
                onNodeOver: function (target, dd, e, dragData) {
                    return this.dropOK ? this.dropAllowed : this.dropNotAllowed;
                },
 
                // Highlight the target node. 
                onNodeOut: function (target, dd, e, dragData) {

					if (typeof onEnter !== 'undefined') {
						onOut(target, dd, e, dragData);
					}

                    var recordEl = me.rowFocus ? target.node.parentElement : target.node;

                    var cls = this.dropOK ? me.dropCls : me.noDropCls;
 
                    if (cls) {
                        Ext.fly(recordEl).removeCls(cls);
                    } else {
                        Ext.fly(recordEl).applyStyles({
                            backgroundColor: ''
                        });
                    }
                },
 
                // Process the drop event if we have previously ascertained that a drop is OK. 
                onNodeDrop: function (target, dd, e, dragData) {
					// A hacky way to ensure that our 'onDrop' function is the one that is called
					if (typeof onDrop !== 'undefined') {
						onDrop(target, dd, e, dragData);
					}
					else {
						if (this.dropOK) {
							target.record.set(target.columnName, dragData.record.get(dragData.columnName));
							if (me.applyEmptyText) {
								dragData.record.set(dragData.columnName, me.emptyText);
							}
							return true;
						}
				}
                },
 
                onCellDrop: Ext.emptyFn
            });
        }
    }
});
