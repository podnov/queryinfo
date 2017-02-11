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


Scenario: Descending Page Iterator

	Given an organization entity query info bean
	When I execute this query for a "DESCENDING" iterator:
	"""
	{
		"paginationInfo": {
			"pageIndex": 0,
			"pageSize": 2
		}
	}
	"""
	Then I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |




Scenario: Ascending Page Iterator

	Given an organization entity query info bean
	When I execute this query for an "ASCENDING" iterator:
	"""
	{
		"paginationInfo": {
			"pageIndex": 0,
			"pageSize": 2
		}
	}
	"""
	Then I should receive these organizations:
	| name      | address1                  | address2 | city          | state | zip   | yearFounded | active |
	| CDW       | 200 N. Milwaukee Ave.     |          | Vernon Hills  | IL    | 60061 | 1984        | true   |
	| Google    | 1600 Amphitheatre Parkway |          | Mountain View | CA    | 94043 | 1998        | true   |
	| Epic      | 1979 Milky Way            |          | Verona        | WI    | 53593 | 1979        | true   |
	| Pets.com  |                           |          | San Francisco | CA    | 94101 | 1998        | false  |
	| Amazon    | 410 Terry Ave. N          |          | Seattle       | WA    | 98109 | 1994        | true   |
	| Facebook  | 1 Hacker Way              |          | Menlo Park    | CA    | 94025 | 2004        | true   |
	| U.S. Navy | The Pentagon              |          | Washington    | DC    | 20001 | 1775        | true   |
