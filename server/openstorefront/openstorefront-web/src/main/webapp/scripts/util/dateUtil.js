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

/* 
 * This is a set Date Manips
 * Singleton
 * 
 * Uses: Ext.Date 
 *	
 */
/* global Ext */

var CoreDateUtil = new Object();
CoreDateUtil.$VERSION = 1.0;

CoreDateUtil.PERIOD_MODE_YEAR = 'Y';
CoreDateUtil.PERIOD_MODE_QUARTER = 'Q';
CoreDateUtil.PERIOD_MODE_MONTH = 'M';
CoreDateUtil.PERIOD_MODE_WEEK = 'W';
CoreDateUtil.PERIOD_MODE_DAY = 'D';

CoreDateUtil.getCurrentYearStart = function(){
	var startYear = new Date();	
	startYear.setMonth(0, 1);
	startYear.setHours(0, 0, 0, 0);	
	return startYear;
};

CoreDateUtil.getCurrentYearEnd = function(){
	var endYear = new Date();		
	endYear.setMonth(11, 31);
	endYear.setHours(23, 59, 59, 999);	
	return endYear;	
};


CoreDateUtil.getCurrentQuarterStart = function(){		
	var startQuarter = new Date();	
	var today = new Date();
	var startMonth = Math.floor(today.getMonth() / 3);
	startMonth = startMonth * 3;
	startQuarter.setMonth(startMonth, 1);
	startQuarter.setHours(0, 0, 0, 0);	
	return startQuarter;	
};

CoreDateUtil.getCurrentQuarterEnd = function(){
	var endQuarter = new Date();	
	var today = new Date();
	var endMonth = Math.floor(today.getMonth() / 3);
	endMonth = endMonth * 3 + 2;
	endQuarter.setMonth(endMonth, 1);
	endQuarter.setDate(Ext.Date.getLastDateOfMonth(endQuarter).getDate());
	endQuarter.setHours(23, 59, 59, 999);	
	return endQuarter;
};

/**
 * TimeConfig *     
 *     periodMode (See statics)
 *     startDts (Date Object of the start of the period)
 *     endDts (Date Object of the end of the period
 * @return: Modifies the startDts and EndDts
 * 
 * @param periodType (String description of Period)
 */
CoreDateUtil.nextPeriod = function(timeConfig){
	CoreDateUtil.nextPreviousPeriod(timeConfig, true);
};

CoreDateUtil.previousPeriod = function(timeConfig){
	CoreDateUtil.nextPreviousPeriod(timeConfig, false);
};

/**
 * 
 * @param periodType (String description of Period)
 */
CoreDateUtil.periodTypeToPeriodMode = function(periodType){	
	if ('YEARLY' === periodType)
	{
		return CoreDateUtil.PERIOD_MODE_YEAR;
	}
	else if ('QUARTERLY' === periodType)
	{
		return CoreDateUtil.PERIOD_MODE_QUARTER;
	}
	else if ('MONTHLY' === periodType)
	{
		return CoreDateUtil.PERIOD_MODE_MONTH;
	}
	else if ('WEEKLY' === periodType)
	{
		return CoreDateUtil.PERIOD_MODE_WEEK;
	}
	else if ('DAILY' === periodType)
	{
		return CoreDateUtil.PERIOD_MODE_DAY;
	}
	return CoreDateUtil.PERIOD_MODE_DAY;
};

CoreDateUtil.drillDownPeriodType = function(periodType){	
	if ('YEARLY' === periodType)
	{
		return 'QUARTERLY';
	}
	else if ('QUARTERLY' === periodType)
	{
		return 'MONTHLY';
	}
	else if ('MONTHLY' === periodType)
	{
		return 'WEEKLY';
	}
	else if ('WEEKLY' === periodType)
	{
		return 'DAILY';
	}		
	//lowest Level
	return 'DAILY';	
};

CoreDateUtil.nextPreviousPeriod = function(timeConfig, next){
	if (CoreDateUtil.PERIOD_MODE_YEAR === timeConfig.periodMode)
	{
		if (next)
		{
			timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.YEAR, 1);
			timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.YEAR, 1);
		}
		else
		{
			timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.YEAR, -1);
			timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.YEAR, -1);			
		}
	}	
	else if (CoreDateUtil.PERIOD_MODE_QUARTER === timeConfig.periodMode)
	{
		if (next)
		{
			if (timeConfig.startDts.getMonth() === 9)
			{
				timeConfig.startDts.setMonth(0);
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.YEAR, 1);				
			}
			else
			{
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.MONTH, 3);
			}
			
			if (timeConfig.endDts.getMonth() === 11)
			{
				timeConfig.endDts.setMonth(2, 1);
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.YEAR, 1);	
			}
			else
			{								
				timeConfig.endDts.setDate(1);
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.MONTH, 3);
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
			}
		}		
		else
		{
			if (timeConfig.startDts.getMonth() === 0)
			{
				timeConfig.startDts.setMonth(9);				
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.YEAR, -1);
			}
			else
			{				
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.MONTH, -3);
			}
			
			if (timeConfig.endDts.getMonth() === 2)
			{
				timeConfig.endDts.setMonth(11, 1);
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.YEAR, -1);	
			}
			else
			{				
				timeConfig.endDts.setDate(1);
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.MONTH, -3);				
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
			}				
		}
	}
	else if (CoreDateUtil.PERIOD_MODE_MONTH === timeConfig.periodMode)
	{
		if (next)
		{
			if (timeConfig.startDts.getMonth() === 11)
			{
				timeConfig.startDts.setMonth(0);
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.YEAR, 1);
			}			
			else
			{	
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.MONTH, 1);
			}
			
			
			if (timeConfig.endDts.getMonth() === 11)
			{
				timeConfig.endDts.setMonth(0, 1);
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.YEAR, 1);	
			}
			else
			{
				timeConfig.endDts.setDate(1);
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.MONTH, 1);
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
			}			
		}
		else
		{
			if (timeConfig.startDts.getMonth() === 0)
			{
				timeConfig.startDts.setMonth(11);				
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.YEAR, -1);
			}			
			else
			{	
				timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.MONTH, -1);
			}
			
			
			if (timeConfig.endDts.getMonth() === 0)
			{
				timeConfig.endDts.setMonth(11, 1);
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.YEAR, -1);
			}
			else
			{
				timeConfig.endDts.setDate(1);
				timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.MONTH, -1);
				timeConfig.endDts.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
			}
		}
	}
	else if (CoreDateUtil.PERIOD_MODE_WEEK === timeConfig.periodMode)
	{
		if (next)
		{
			timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.DAY, 7);
			timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.DAY, 7);
		}
		else
		{
			timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.DAY, -7);
			timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.DAY, -7);
		}			
	}	
	else if (CoreDateUtil.PERIOD_MODE_DAY === timeConfig.periodMode)
	{
		if (next)
		{
			timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.DAY, 1);
			timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.DAY, 1);
		}
		else
		{
			timeConfig.startDts = Ext.Date.add(timeConfig.startDts, Ext.Date.DAY, -1);
			timeConfig.endDts = Ext.Date.add(timeConfig.endDts, Ext.Date.DAY, -1);				
		}			
	}
	
};

CoreDateUtil.adjustTimeToContainingPeriod = function(timeConfig){
	
	if (CoreDateUtil.PERIOD_MODE_YEAR === timeConfig.periodMode)
	{
		//begining of year and end of year		
		timeConfig.startDts.setMonth(0, 1);
		timeConfig.startDts.setHours(0, 0, 0, 0);	
		
		var newDate = new Date(timeConfig.startDts);		
		newDate.setMonth(11, 1);
		newDate.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
		newDate.setHours(23, 59, 59, 999);
		timeConfig.endDts = newDate;				
	}	
	else if (CoreDateUtil.PERIOD_MODE_QUARTER === timeConfig.periodMode)
	{
		//begining of quarter and end of quarter
		var startQuarter = new Date();	
		var today = new Date(timeConfig.startDts);
		var startMonth = Math.floor(today.getMonth() / 3);
		startMonth = startMonth * 3;
		startQuarter.setMonth(startMonth, 1);
		startQuarter.setHours(0, 0, 0, 0);	
		timeConfig.startDts = startQuarter;	
		timeConfig.startDts.setFullYear(today.getFullYear());

		var endQuarter = new Date();	
		today = new Date(timeConfig.startDts);
		
		var endMonth = Math.floor(today.getMonth() / 3);
		endMonth = endMonth * 3 + 2;
		endQuarter.setMonth(endMonth, 1);
		endQuarter.setDate(Ext.Date.getLastDateOfMonth(endQuarter).getDate());
		endQuarter.setHours(23, 59, 59, 999);			
		timeConfig.endDts = endQuarter;
		timeConfig.endDts.setFullYear(today.getFullYear()); 
	}
	else if (CoreDateUtil.PERIOD_MODE_MONTH === timeConfig.periodMode)
	{
		//begining of month and end of month
		timeConfig.startDts.setDate(1);
		timeConfig.startDts.setHours(0, 0, 0, 0);
		
		var newDate = new Date(timeConfig.startDts);
		newDate.setDate(Ext.Date.getLastDateOfMonth(timeConfig.startDts).getDate());
		newDate.setHours(23, 59, 59, 999);		
		timeConfig.endDts = newDate;
		
	}
	else if (CoreDateUtil.PERIOD_MODE_WEEK === timeConfig.periodMode)
	{
		//begining of week and end of week
		var day = timeConfig.startDts.getDay(),
		 diff = timeConfig.startDts.getDate() - day + (day === 0 ? -6 : 1); // adjust when day is sunday
		
		timeConfig.startDts.setDate(diff);		
		timeConfig.startDts.setHours(0, 0, 0, 0);
		
		var newDate = new Date(timeConfig.startDts);
		newDate.setDate(timeConfig.startDts.getDate() + 7);
		newDate.setHours(23, 59, 59, 999);		
		timeConfig.endDts = newDate;		
	}	
	else if (CoreDateUtil.PERIOD_MODE_DAY === timeConfig.periodMode)
	{
		//begining of day and end of day		
		timeConfig.startDts.setHours(0, 0, 0, 0);	
		
		timeConfig.endDts.setDate(timeConfig.startDts.getDate());
		timeConfig.endDts.setHours(23, 59, 59, 999);
	}	
	
};

CoreDateUtil.adjustTimeToCurrentPeriod = function(timeConfig){
	
	if (CoreDateUtil.PERIOD_MODE_YEAR === timeConfig.periodMode)
	{
		var today = new Date();	
		//begining of year and end of year				
		timeConfig.startDts.setMonth(0, 1);
		timeConfig.startDts.setHours(0, 0, 0, 0);	
		timeConfig.startDts.setFullYear(today.getFullYear());
		
		var newDate = new Date(timeConfig.startDts);		
		newDate.setMonth(11, 1);
		newDate.setDate(Ext.Date.getLastDateOfMonth(timeConfig.endDts).getDate());
		newDate.setHours(23, 59, 59, 999);
		timeConfig.endDts.setFullYear(today.getFullYear()); 
		
		timeConfig.endDts = newDate;				
	}	
	else if (CoreDateUtil.PERIOD_MODE_QUARTER === timeConfig.periodMode)
	{
		//begining of quarter and end of quarter
		timeConfig.startDts = CoreDateUtil.getCurrentQuarterStart();
		timeConfig.endDts = CoreDateUtil.getCurrentQuarterEnd();
	}
	else if (CoreDateUtil.PERIOD_MODE_MONTH === timeConfig.periodMode)
	{
		var today = new Date();		
				
		//begining of month and end of month		
		timeConfig.startDts.setMonth(today.getMonth(), 1);
		timeConfig.startDts.setHours(0, 0, 0, 0);
		timeConfig.endDts.setFullYear(today.getFullYear()); 
		
		var newDate = new Date(timeConfig.startDts);
		newDate.setMonth(today.getMonth(), Ext.Date.getLastDateOfMonth(timeConfig.startDts).getDate());
		newDate.setHours(23, 59, 59, 999);		
		timeConfig.endDts = newDate;
		timeConfig.endDts.setFullYear(today.getFullYear()); 
		
	}
	else if (CoreDateUtil.PERIOD_MODE_WEEK === timeConfig.periodMode)
	{
		var today = new Date();
		
		//begining of week and end of week
		var day = today.getDay(),
		 diff = today.getDate() - day + (day === 0 ? -6 : 1); // adjust when day is sunday
		
		today.setDate(diff);		
		today.setHours(0, 0, 0, 0);
		timeConfig.startDts = today;
		
		
		var newDate = new Date(timeConfig.startDts);
		newDate.setDate(timeConfig.startDts.getDate() + 7);
		newDate.setHours(23, 59, 59, 999);		
		timeConfig.endDts = newDate;		
	}	
	else if (CoreDateUtil.PERIOD_MODE_DAY === timeConfig.periodMode)
	{
		var today = new Date();
		timeConfig.startDts = today;
		//begining of day and end of day		
		timeConfig.startDts.setHours(0, 0, 0, 0);	
		
		timeConfig.endDts.setDate(timeConfig.startDts.getDate());
		timeConfig.endDts.setHours(23, 59, 59, 999);
	}	
	
};

CoreDateUtil.startOfDay = function(inDts)
{
	var newDate = new Date(inDts);	
	newDate.setHours(0, 0, 0, 0);
	return newDate;
};

CoreDateUtil.endOfDay = function(inDts)
{
	var newDate = new Date(inDts);	
	newDate.setHours(23, 59, 59, 999);
	return newDate;
};


