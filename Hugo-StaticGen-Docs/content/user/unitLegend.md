+++
title = "Unit Legend"
description = ""
weight = 100
hasMath = true
markup = "mmark"
+++

This is the legend for commonly confused unit names and acronyms.
<!--more-->

| Symbol           | Base unit                          | Description                                       |
| ---------------- | ---------------------------------- | ------------------------------------------------- |
| '$'              | US Dollar                          | 1 US dollar                                       |
| 'G'              | Gauss                              | 1 Gauss                                           |
| 'GB'             | Byte                               | $10^9$ bytes (Gigabyte)                           |
| 'Gb'             | Bit                                | $10^9$ bits (Gigabit)                             |
| 'Gbps'           | Bit Per Second                     | $10^9 \frac{\text{bits}}{\text{sec}}$             |
| 'Grav'           | Newtonian constant of gravitation  |
| 'MB'             | Byte                               | $10^6$ bytes (Megabyte)                           |
| 'MIPS'           | Million of Instructions Per Second | $10^6$ $\frac{\text{instruction}}{\text{second}}$ |
| 'Mb'             | Bit                                | $10^6$ bits (Megabit)                             |
| 'Mbps'           | Bit Per Second                     | $10^6 \frac{\text{bits}}{\text{sec}}$             |
| 'PB'             | Byte                               | $10^{15}$ bytes (Petabyte)                        |
| 'Pb'             | Bit                                | $10^{15}$ bits (Petabit)                          |
| 'TB'             | Byte                               | $10^{12}$ bytes (Terabyte)                        |
| 'Tb'             | Bit                                | $10^{12}$ bits (Terabit)                          |
| 'arcsec'         | Degree                             | $\frac{1}{3600}$ degree                           |
| 'arcsmin'        | Degree                             | $\frac{1}{60}$ degree                             |
| 'bps'            | Bit Per Second                     | 1 $\frac{\text{bit}}{\text{sec}}$                 |
| 'dBW'            | Decibel Watt                       | 1 decibel Watt                                    |
| 'dBm'            | Decibel Watt                       | $10^{-6}$ decibel Watt (Decibel milli-Watt)       |
| 'dbC'            | Decibel                            | 1 Decibel                                         |
| 'dbc'            | Decibel                            | 1 Decibel                                         |
| 'dbd'            | Decibel                            | 1 Decibel                                         |
| 'dbi'            | Decibel                            | 1 Decibel                                         |
| 'deg' or '°'     | Degree                             | 1 degree                                          |
| 'g'              | Gram                               | 1 gram                                            |
| 'grms' or 'Grms' | Root-Mean-Square Acceleration      |
| 'hr' or 'hour'   | Hour                               | 1 hour                                            |
| 'inch'           | inch                               | 1 inch                                            |
| 'kB' or 'KB'     | Byte                               | $10^3$ bytes (Kilobyte)                           |
| 'kb' or 'Kb'     | Bit                                | $10^3$ bits (Kilobit)                             |
| 'kbps'           | Bit Per Second                     | $10^3 \frac{\text{bits}}{\text{sec}}$             |
| 'mG'             | Gauss                              | $10^{-6}$ Gauss (microGauss)                      |
| 'mGrav'          | Grav                               | $10^{-6}$ Grav (microGrav)                        |
| 'micron' or 'μm' | Meter                              | $10^{-6}$ meter (micrometer)                      |
| 'psi'            | Pounds Per Square Inch             | 1 $\frac{\text{lbs}}{\text{in}^2}$                |
| 'rpm' or 'RPM'   | Revolution Per Minute              | 1 $\frac{\text{revolution}}{\text{Minute}}$       |
| 'μN'             | Newton                             | $10^{-6}$ Newton (microNewton)                    |
| 'μm'             | Gram                               | $10^{-6}$ gram (Microgram)                        |
| 'μs'             | Second                             | $10^{-6}$ second (Microsecond)                    |

## JScience Semantics

When attempting to use a superscript fraction as an attribute (ex. 'deg/(hr^(1/2))'), it will not be able to parse that. In order to get it to parse you must use a colon (ex. 'deg/(hr^1:2)'). This will result in it parsing correctly and it will render as $deg/(hr^{1/2})$.
