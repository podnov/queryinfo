Feature: Query with conditions

Background:

	Given these companies:
	| name   | address1                  | address2 | city          | state | zip   | yearFounded |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        |
	| Google | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        |
	| Epic   | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        |


Scenario: Basic less than or equal to query

	Given the companies query info web service
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
	And I should receive these companies:
	| name   | address1                  | address2 | city          | state | zip   | yearFounded |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        |
	| Epic   | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        |

Scenario: Basic less than to query

	Given the companies query info web service
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
	And I should receive these companies:
	| name   | address1                  | address2 | city          | state | zip   | yearFounded |
	| Epic   | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        |

Scenario: Basic greater than or equal to query

	Given the companies query info web service
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
	And I should receive these companies:
	| name   | address1                  | address2 | city          | state | zip   | yearFounded |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        |
	| Google | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        |

Scenario: Basic greater than query

	Given the companies query info web service
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
	And I should receive these companies:
	| name   | address1                  | address2 | city          | state | zip   | yearFounded |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        |
	| Google | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        |

Scenario: Basic like query

	Given the companies query info web service
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
	And I should receive these companies:
	| name   | address1                  | address2 | city          | state | zip   | yearFounded |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        |
	| Epic   | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        |

Scenario: Basic equality query

	Given the companies query info web service
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
	And I should receive these companies:
	| name   | address1                  | address2 | city          | state | zip   | yearFounded |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        |