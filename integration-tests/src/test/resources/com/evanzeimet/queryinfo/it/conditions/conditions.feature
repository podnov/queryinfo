Feature: Query with conditions

Background:

	Given these organizations:
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW      | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Google   | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Epic     | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| Pets.com |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Amazon   | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

	And these people:
	| firstName | lastName   | employerOrganizationName |
	| Evan      | Zeimet     | CDW                      |
	| Larry     | Page       | Google                   |
	| Judith    | Faulkner   | Epic                     |
	| Jeff      | Bezos      | Amazon                   |
	| Mark      | Zuckerberg | Facebook                 |
	#| Pete      | Mitchell   | U.S. Navy                |


Scenario: Condition for joined field

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

Scenario: Nested conditions

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
					"rightHandSide": "true"
				}
			],
			"operator": "and"
		}
	}
	"""
	Then the http response code should be 200
	And I should receive these organizations:
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Google   | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Amazon   | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW      | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Epic     | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Epic     | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW      | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Epic     | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| Epic     | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW      | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Google   | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Pets.com |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Amazon   | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW      | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Google   | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Pets.com |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Amazon   | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW      | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Epic     | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| Pets.com |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Facebook | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |

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
	| name     | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW      | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
