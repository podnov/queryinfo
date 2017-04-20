Feature: Query with QueryInfo

Background:

	Given these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active | dateCreated         |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   | 2015-10-09T00:00:00 |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   | 2015-10-10T00:00:00 |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   | 2015-10-11T00:00:00 |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  | 2015-10-12T00:00:00 |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   | 2015-10-13T00:00:00 |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   | 2015-10-14T00:00:00 |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   | 2015-10-15T00:00:00 |

	And these people:
	| firstName | lastName   | employerOrganizationName |
	| Evan      | Zeimet     | CDW                      |
	| Larry     | Page       | Google                   |
	| Judith    | Faulkner   | Epic                     |
	| Jeff      | Bezos      | Amazon                   |
	| Mark      | Zuckerberg | Facebook                 |
	| Pete      | Mitchell   | U.S. Navy                |
	| Nick      | Bradshaw   | U.S. Navy                |
	| Tom       | Kazanski   | U.S. Navy                |
	| Mike      | Metcalf    | U.S. Navy                |
	| I'm       | Unemployed |                          |



Scenario: exists query, with subquery name, chained not exists query, people that work for an employer that does not have an employee named Pete Mitchell

	Given the people query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"directive": "EXISTS",
			"directiveConfig": {
				"subqueryRootAttributePath": "employer",
				"subqueryName": "allEmployers"
			},
			"conditionGroups": [
				{
					"directive": "NOT_EXISTS",
					"directiveConfig": {
						"subqueryRootAttributePath": "employees"
					},
					"conditions": [
						{
							"leftHandSide": "firstName",
							"operator": "=",
							"rightHandSide": "Pete"
						},
						{
							"leftHandSide": "lastName",
							"operator": "=",
							"rightHandSide": "Mitchell"
						},
						{
							"leftHandSide": "employerOrganizationId",
							"operator": "=",
							"rightHandSide": "id",
							"rightHandSideType": "ATTRIBUTE_PATH",
							"rightHandSideTypeConfig": "allEmployers"
						}
					]
				}
			],
			"conditions": [
				{
					"leftHandSide": "id",
					"operator": "=",
					"rightHandSide": "employer.id",
					"rightHandSideType": "ATTRIBUTE_PATH",
					"rightHandSideTypeConfig": "root"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Evan      | Zeimet     |
	| Larry     | Page       |
	| Judith    | Faulkner   |
	| Jeff      | Bezos      |
	| Mark      | Zuckerberg |


Scenario: Exists query, with subquery name, chained exists query, people that aren't named Pete Mitchell that work for an employer that has an employee named Pete Mitchell

	Given the people query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditionGroups": [
				{
					"directive": "EXISTS",
					"directiveConfig": {
						"subqueryRootAttributePath": "employer",
						"subqueryName": "allEmployers"
					},
					"conditionGroups": [
						{
							"directive": "EXISTS",
							"directiveConfig": {
								"subqueryRootAttributePath": "employees"
							},
							"conditions": [
								{
									"leftHandSide": "firstName",
									"operator": "=",
									"rightHandSide": "Pete"
								},
								{
									"leftHandSide": "lastName",
									"operator": "=",
									"rightHandSide": "Mitchell"
								},
								{
									"leftHandSide": "employerOrganizationId",
									"operator": "=",
									"rightHandSide": "id",
									"rightHandSideType": "ATTRIBUTE_PATH",
									"rightHandSideTypeConfig": "allEmployers"
								}
							]
						}
					],
					"conditions": [
						{
							"leftHandSide": "id",
							"operator": "=",
							"rightHandSide": "employer.id",
							"rightHandSideType": "ATTRIBUTE_PATH",
							"rightHandSideTypeConfig": "root"
						}
					]
				},
				{
					"conditions": [
						{
							"leftHandSide": "firstName",
							"operator": "<>",
							"rightHandSide": "Pete"
						},
						{
							"leftHandSide": "lastName",
							"operator": "<>",
							"rightHandSide": "Mitchell"
						}
					],
					"operator": "and"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Nick      | Bradshaw   |
	| Tom       | Kazanski   |
	| Mike      | Metcalf    |


Scenario: Exists query, no subquery name, people that aren't named Pete Mitchell that work for an employer that has an employee named Pete Mitchell

	Given the people query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditionGroups": [
				{
					"directive": "EXISTS",
					"directiveConfig": {
						"subqueryRootAttributePath": "employer"
					},
					"conditions": [
						{
							"leftHandSide": "employees.firstName",
							"operator": "=",
							"rightHandSide": "Pete"
						},
						{
							"leftHandSide": "employees.lastName",
							"operator": "=",
							"rightHandSide": "Mitchell"
						},
						{
							"leftHandSide": "id",
							"operator": "=",
							"rightHandSide": "employer.id",
							"rightHandSideType": "ATTRIBUTE_PATH",
							"rightHandSideTypeConfig": "root"
						}
					]
				},
				{
					"conditions": [
						{
							"leftHandSide": "firstName",
							"operator": "<>",
							"rightHandSide": "Pete"
						},
						{
							"leftHandSide": "lastName",
							"operator": "<>",
							"rightHandSide": "Mitchell"
						}
					],
					"operator": "and"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Nick      | Bradshaw   |
	| Tom       | Kazanski   |
	| Mike      | Metcalf    |


Scenario: First Page

	Given the generic query info web service for the "organizationEntity"
	When I send the query:
	"""
	{
		"paginationInfo": {
			"pageIndex": 0,
			"pageSize": 2
		},
		"sorts":[
			{
				"attributePath": "name",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive this json:
	"""
	{
	  "pageResults" : [
	    {
	      "@id" : 1,
	      "active" : true,
	      "address1" : "410 Terry Ave. N",
	      "address2" : "",
	      "city" : "Seattle",
	      "dateCreated" : 1444694400000,
	      "id" : 5,
	      "name" : "Amazon",
	      "state" : "WA",
	      "yearFounded" : 1994,
	      "zip" : "98109",
	      "employees" : null
	    },
	    {
	      "@id" : 2,
	      "active" : true,
	      "address1" : "200 N. Milwaukee Ave.",
	      "address2" : "",
	      "city" : "Vernon Hills",
	      "dateCreated" : 1444348800000,
	      "id" : 1,
	      "name" : "CDW",
	      "state" : "IL",
	      "yearFounded" : 1984,
	      "zip" : "60061",
	      "employees" : null
	    }
	  ],
	  "totalCount" : 7
	}
	"""

Scenario: Middle Page

	Given the generic query info web service for the "organizationEntity"
	When I send the query:
	"""
	{
		"paginationInfo": {
			"pageIndex": 1,
			"pageSize": 2
		},
		"sorts":[
			{
				"attributePath": "name",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive this json:
	"""
	{
	  "pageResults" : [
	    {
	      "@id" : 1,
	      "active" : true,
	      "address1" : "1979 Milky Way",
	      "address2" : "",
	      "city" : "Verona",
	      "dateCreated" : 1444521600000,
	      "id" : 3,
	      "name" : "Epic",
	      "state" : "WI",
	      "yearFounded" : 1979,
	      "zip" : "53593",
	      "employees" : null
	    },
	    {
	      "@id" : 2,
	      "active" : true,
	      "address1" : "1 Hacker Way",
	      "address2" : "",
	      "city" : "Menlo Park",
	      "dateCreated" : 1444780800000,
	      "id" : 6,
	      "name" : "Facebook",
	      "state" : "CA",
	      "yearFounded" : 2004,
	      "zip" : "94025",
	      "employees" : null
	    }
	  ],
	  "totalCount" : 7
	}
	"""

Scenario: Last Page

	Given the generic query info web service for the "organizationEntity"
	When I send the query:
	"""
	{
		"paginationInfo": {
			"pageIndex": 3,
			"pageSize": 2
		},
		"sorts":[
			{
				"attributePath": "name",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive this json:
	"""
	{
	  "pageResults" : [
	    {
	      "@id" : 1,
	      "active" : true,
	      "address1" : "The Pentagon",
	      "address2" : "",
	      "city" : "Washington",
	      "dateCreated" : 1444867200000,
	      "id" : 7,
	      "name" : "U.S. Navy",
	      "state" : "DC",
	      "yearFounded" : 1775,
	      "zip" : "20001",
	      "employees" : null
	    }
	  ],
	  "totalCount" : 7
	}
	"""

Scenario: Group by with avg aggregate function

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"selections": [
			{
				"attributePath": "yearFounded",
				"aggregateFunction": "avg"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| yearFounded |
	| 1961.7143   |

Scenario: Group by with aggregate function on joined attribute

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "active",
					"operator": "=",
					"rightHandSide": true
				}
			]
		},
		"groupByAttributePaths": [
			"name"
		],
		"selections": [
			{
				"attributePath": "name"
			},
			{
				"attributePath": "employees.firstName",
				"aggregateFunction": "count"
			}
		],
		"sorts":[
			{
				"attributePath": "name",
				"direction": "desc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| name      | employees.firstName |
	| U.S. Navy | 4                   |
	| Google    | 1                   |
	| Facebook  | 1                   |
	| Epic      | 1                   |
	| CDW       | 1                   |
	| Amazon    | 1                   |


Scenario: Group by with count aggregate function

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"groupByAttributePaths": [
			"state"
		],
		"selections": [
			{
				"attributePath": "state"
			},
			{
				"attributePath": "name",
				"aggregateFunction": "count"
			}
		],
		"sorts":[
			{
				"attributePath": "state",
				"direction": "desc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| state   | name |
	| WI      | 1    |
	| WA      | 1    |
	| IL      | 1    |
	| DC      | 1    |
	| CA      | 3    |

Scenario: Generic entity resource test

	Given the generic query info web service for the "personEntity"
	When I send the query:
	"""
	{
		"sorts":[
			{
				"attributePath": "lastName",
				"direction": "asc"
			},
			{
				"attributePath": "firstName",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these paginated people:
	| firstName | lastName                  |
	| Jeff      | Bezos                     |
	| Nick      | Bradshaw                  |
	| Judith    | Faulkner                  |
	| Tom       | Kazanski                  |
	| Mike      | Metcalf                   |
	| Pete      | Mitchell                  |
	| Larry     | Page                      |
	| I'm       | Unemployed                |
	| Evan      | Zeimet                    |
	| Mark      | Zuckerberg                |


Scenario: Generic entity resource test

	Given the generic query info web service for the "organizationEntity"
	When I send the query:
	"""
	{
		"sorts":[
			{
				"attributePath": "name",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these paginated organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |

Scenario: Tuple to pojo conversion with requested fields

	Given the organization employees query info web service
	When I send the query:
	"""
	{
		"selections": [
			{
				"attributePath": "employees.lastName"
			},
			{
				"attributePath": "name"
			}
		],
		"sorts":[
			{
				"attributePath": "name",
				"direction": "asc"
			},
			{
				"attributePath": "employees.lastName",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| employeesFirstName | employeesLastName | name      |
	| [[NULL]]           | Bezos             | Amazon    |
	| [[NULL]]           | Zeimet            | CDW       |
	| [[NULL]]           | Faulkner          | Epic      |
	| [[NULL]]           | Zuckerberg        | Facebook  |
	| [[NULL]]           | Page              | Google    |
	| [[NULL]]           | [[NULL]]          | Pets.com  |
	| [[NULL]]           | Bradshaw          | U.S. Navy |
	| [[NULL]]           | Kazanski          | U.S. Navy |
	| [[NULL]]           | Metcalf           | U.S. Navy |
	| [[NULL]]           | Mitchell          | U.S. Navy |

Scenario: Tuple to pojo conversion

	Given the organization employees query info web service
	When I send the query:
	"""
	{
		"sorts":[
			{
				"attributePath": "name",
				"direction": "asc"
			},
			{
				"attributePath": "employees.lastName",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| employeesFirstName | employeesLastName | name      |
	| Jeff               | Bezos             | Amazon    |
	| Evan               | Zeimet            | CDW       |
	| Judith             | Faulkner          | Epic      |
	| Mark               | Zuckerberg        | Facebook  |
	| Larry              | Page              | Google    |
	| [[NULL]]           | [[NULL]]          | Pets.com  |
	| Nick               | Bradshaw          | U.S. Navy |
	| Tom                | Kazanski          | U.S. Navy |
	| Mike               | Metcalf           | U.S. Navy |
	| Pete               | Mitchell          | U.S. Navy |
	
Scenario: Basic group by

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"groupByAttributePaths": [
			"state"
		],
		"selections": [
			{
				"attributePath": "state"
			}
		],
		"sorts":[
			{
				"attributePath": "state",
				"direction": "desc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| state   |
	| WI      |
	| WA      |
	| IL      |
	| DC      |
	| CA      |

Scenario: Use nested requested fields, one-to-many join

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"selections": [
			{
				"attributePath": "name"
			},
			{
				"attributePath": "state"
			},
			{
				"attributePath": "employees.firstName"
			},
			{
				"attributePath": "employees.lastName"
			}
		],
		"sorts":[
			{
				"attributePath": "name",
				"direction": "desc"
			},
			{
				"attributePath": "employees.lastName",
				"direction": "asc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| name          | state   | employees.firstName | employees.lastName |
	| U.S. Navy     | DC      | Nick                | Bradshaw           |
	| U.S. Navy     | DC      | Tom                 | Kazanski           |
	| U.S. Navy     | DC      | Mike                | Metcalf            |
	| U.S. Navy     | DC      | Pete                | Mitchell           |
	| Pets.com      | CA      | [[NULL]]            | [[NULL]]           |
	| Google        | CA      | Larry               | Page               |
	| Facebook      | CA      | Mark                | Zuckerberg         |
	| Epic          | WI      | Judith              | Faulkner           |
	| CDW           | IL      | Evan                | Zeimet             |
	| Amazon        | WA      | Jeff                | Bezos              |

Scenario: Use nested requested fields, one-to-one join

	Given the people query info web service
	When I send the query:
	"""
	{
		"selections": [
			{
				"attributePath": "firstName"
			},
			{
				"attributePath": "lastName"
			},
			{
				"attributePath": "employer"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| firstName | lastName   | employer       |
	| Evan      | Zeimet     | [[ANY_OBJECT]] |
	| Larry     | Page       | [[ANY_OBJECT]] |
	| Judith    | Faulkner   | [[ANY_OBJECT]] |
	| Jeff      | Bezos      | [[ANY_OBJECT]] |
	| Mark      | Zuckerberg | [[ANY_OBJECT]] |
	| Pete      | Mitchell   | [[ANY_OBJECT]] |
	| Nick      | Bradshaw   | [[ANY_OBJECT]] |
	| Tom       | Kazanski   | [[ANY_OBJECT]] |
	| Mike      | Metcalf    | [[ANY_OBJECT]] |
	| I'm       | Unemployed | [[NULL]]       |

Scenario: Use nested requested fields, one-to-one join

	Given the people query info web service
	When I send the query:
	"""
	{
		"selections": [
			{
				"attributePath": "firstName"
			},
			{
				"attributePath": "lastName"
			},
			{
				"attributePath": "employer.name"
			},
			{
				"attributePath": "employer.state"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| firstName | lastName   | employer.name | employer.state |
	| Evan      | Zeimet     | CDW           | IL             |
	| Larry     | Page       | Google        | CA             |
	| Judith    | Faulkner   | Epic          | WI             |
	| Jeff      | Bezos      | Amazon        | WA             |
	| Mark      | Zuckerberg | Facebook      | CA             |
	| Pete      | Mitchell   | U.S. Navy     | DC             |
	| Nick      | Bradshaw   | U.S. Navy     | DC             |
	| Tom       | Kazanski   | U.S. Navy     | DC             |
	| Mike      | Metcalf    | U.S. Navy     | DC             |
	| I'm       | Unemployed | [[NULL]]      | [[NULL]]       |

Scenario: Use requested fields

	Given the people query info web service
	When I send the query:
	"""
	{
		"selections": [
			{
				"attributePath": "firstName"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these tuples:
	| firstName |
	| Evan      |
	| Larry     |
	| Judith    |
	| Jeff      |
	| Mark      |
	| Pete      |
	| Nick      |
	| Tom       |
	| Mike      |
	| I'm       |

Scenario: Paginate some sorted stuff (page 2)

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"paginationInfo": {
			"pageIndex": 2,
			"pageSize": 3
		},
		"sorts": [
			{
				"attributePath": "state",
				"direction": "asc"
			},
			{
				"attributePath": "zip",
				"direction": "desc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

Scenario: Paginate some sorted stuff (page 1)

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"paginationInfo": {
			"pageIndex": 1,
			"pageSize": 3
		},
		"sorts": [
			{
				"attributePath": "state",
				"direction": "asc"
			},
			{
				"attributePath": "zip",
				"direction": "desc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |

Scenario: Paginate some sorted stuff (page 0)

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"paginationInfo": {
			"pageIndex": 0,
			"pageSize": 3
		},
		"sorts": [
			{
				"attributePath": "state",
				"direction": "asc"
			},
			{
				"attributePath": "zip",
				"direction": "desc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

Scenario: Sort some stuff

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"sorts": [
			{
				"attributePath": "state",
				"direction": "asc"
			},
			{
				"attributePath": "zip",
				"direction": "desc"
			}
		]
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

Scenario: Condition for date field

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "dateCreated",
					"operator": ">",
					"rightHandSide": "2015-10-12T00:00:00"
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |


Scenario: Condition with not in operator

	Given the people query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "employer.state",
					"operator": "not in",
					"rightHandSide": ["IL","WI"]
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Larry     | Page       |
	| Jeff      | Bezos      |
	| Mark      | Zuckerberg |
	| Pete      | Mitchell   |
	| Nick      | Bradshaw   |
	| Tom       | Kazanski   |
	| Mike      | Metcalf    |

Scenario: Condition with in operator

	Given the people query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "employer.state",
					"operator": "in",
					"rightHandSide": ["IL","WI"]
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Evan      | Zeimet     |
	| Judith    | Faulkner   |

Scenario: Empty query info

	Given the people query info web service
	When I send the query:
	"""
	{}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Evan      | Zeimet     |
	| Larry     | Page       |
	| Judith    | Faulkner   |
	| Jeff      | Bezos      |
	| Mark      | Zuckerberg |
	| Pete      | Mitchell   |
	| Nick      | Bradshaw   |
	| Tom       | Kazanski   |
	| Mike      | Metcalf    |
	| I'm       | Unemployed |

Scenario: Double joined field, people that aren't named Pete Mitchell that work for an employer that has an employee named Pete Mitchell

	Given the people query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "employer.employees.firstName",
					"operator": "=",
					"rightHandSide": "Pete"
				},
				{
					"leftHandSide": "employer.employees.lastName",
					"operator": "=",
					"rightHandSide": "Mitchell"
				},
				{
					"leftHandSide": "firstName",
					"operator": "<>",
					"rightHandSide": "Pete"
				},
				{
					"leftHandSide": "lastName",
					"operator": "<>",
					"rightHandSide": "Mitchell"
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Nick      | Bradshaw   |
	| Tom       | Kazanski   |
	| Mike      | Metcalf    |

Scenario: Condition for joined field, employers that have an employee named Pete Mitchell

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "employees.firstName",
					"operator": "=",
					"rightHandSide": "Pete"
				},
				{
					"leftHandSide": "employees.lastName",
					"operator": "=",
					"rightHandSide": "Mitchell"
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |

Scenario: Condition for joined field, people who's employer is out of CA

	Given the people query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "employer.state",
					"operator": "=",
					"rightHandSide": "CA"
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these people:
	| firstName | lastName   |
	| Larry     | Page       |
	| Mark      | Zuckerberg |


Scenario: Nested conditions, organization is active and ( is out of CA or WA )

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditionGroups": [
				{
					"conditions": [
						{
							"leftHandSide": "state",
							"operator": "=",
							"rightHandSide": "CA"
						},
						{
							"leftHandSide": "state",
							"operator": "=",
							"rightHandSide": "WA"
						}
					],
					"operator": "or"
				}
			],
			"conditions": [
				{
					"leftHandSide": "active",
					"operator": "=",
					"rightHandSide": true
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

Scenario: Multiple conditions with or

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "state",
					"operator": "=",
					"rightHandSide": "IL"
				},
				{
					"leftHandSide": "state",
					"operator": "=",
					"rightHandSide": "WI"
				}
			],
			"operator": "or"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

Scenario: Multiple conditions with and

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "yearFounded",
					"operator": "<=",
					"rightHandSide": "1984"
				},
				{
					"leftHandSide": "state",
					"operator": "=",
					"rightHandSide": "WI"
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

Scenario: Basic less than or equal to query

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "yearFounded",
					"operator": "<=",
					"rightHandSide": "1984"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |

Scenario: Basic less than to query

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "yearFounded",
					"operator": "<",
					"rightHandSide": "1984"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |

Scenario: Basic greater than or equal to query

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "yearFounded",
					"operator": ">=",
					"rightHandSide": "1984"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

Scenario: Basic greater than query

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "yearFounded",
					"operator": ">",
					"rightHandSide": "1979"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

Scenario: Basic like query

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "name",
					"operator": "like",
					"rightHandSide": "%c%"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

Scenario: Basic equality query

	Given the organizations query info web service
	When I send the query:
	"""
	{
		"conditionGroup": {
			"conditions": [
				{
					"leftHandSide": "name",
					"operator": "=",
					"rightHandSide": "CDW"
				}
			]
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
