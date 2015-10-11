Feature: Companies Query

Background:

	Given these companies:
	| name   | address1                  | address2 | city          | state | zip   |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 |
	| Google | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 |
	| Epic   | 1979 Milky Way            |          | Verona        | WI    | 53593 |

Scenario: Basic query

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
	| name   | address1                  | address2 | city          | state | zip   |
	| CDW    | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 |