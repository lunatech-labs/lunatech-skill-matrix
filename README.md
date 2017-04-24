# Lunatech Tech Matrix ![Build Status](http://jenkins.lunatech.com/job/TechMatrix/badge/icon)

# Acceptance test
You can use `sbt acc:test` command to run only the acceptance tests, or `sbt test` to run all the tests.

# Data Model
In the application there are a few entities we use:

**User**:  
&nbsp;&nbsp;&nbsp;&nbsp;id  
&nbsp;&nbsp;&nbsp;&nbsp;firstName  
&nbsp;&nbsp;&nbsp;&nbsp;lastName  
&nbsp;&nbsp;&nbsp;&nbsp;email  

**Tech**:  
&nbsp;&nbsp;&nbsp;&nbsp;id  
&nbsp;&nbsp;&nbsp;&nbsp;name  
&nbsp;&nbsp;&nbsp;&nbsp;techType  

**Skill**:  
&nbsp;&nbsp;&nbsp;&nbsp;id  
&nbsp;&nbsp;&nbsp;&nbsp;userId  
&nbsp;&nbsp;&nbsp;&nbsp;techId  
&nbsp;&nbsp;&nbsp;&nbsp;skillLevel  

In our database we have 3 main tables: *users*, *tech* and *user_skills*.
The third table is just a holder of userId, techId and the level.
When the users introduces a skill to his list, the properties of the skill (the name and type) are stored in the *Tech* table, and therefore form the tech body.
We store it in the *Tech* table so that this particular skill will be available for other users as well (autocomplete)

So the difference between the *Tech* and *Skill* is that the tech represents the technical knowledge a user has, and the skill is just a holder of the techId & userId and the level that user has about that skill.

### How it looks in the database:
**Users**:  

|id      | firstName | lastName  | email                       |
|--------|-----------|-----------|-----------------------------|
|1       | Martin     | Odersky  | martin.odersky@scala.com |
|2       | Severus   | Snape     | severus.snape@hogwarts.com  |

**Tech**:  

|id      | name      | type       |
|--------|-----------|------------|
|1       | Scala     | LANGUAGE   |
|2       | Dark Arts | CONCEPT |

**Skill**:  

|id      | userId    | techId    | skillLevel  |
|--------|-----------|-----------|-------------|
|1       | 1         | 1         | INTERMEDIATE |
|2       | 2         | 2         | EXPERT   |

We also have intermediate models that help to either intercept correctly the parameters in the controller or to hold the results needed.

*SkillMatrixItem*:  
&nbsp;&nbsp;&nbsp;&nbsp;Tech  
&nbsp;&nbsp;&nbsp;&nbsp;SkillLevel

This is used in the POST a users makes in order to add a skill to his list. It looks like this:
```json 
{
	"tech": {
		"name": "Prolog",
		"techType": "LANGUAGE"
	},
	"skillLevel": "NOVICE"
}
```

At the moment of writing, we haven't found yet a way on how to validate that the request contains 2 objects (tech and skillLevel), hence we create an intermediate case class that will hold these two objects.

We also have two response models. These are used to create correctly the results of some GET requests, because we mix the data from 2 or 3 tables in one response.

*SkillMatrixResponse*:  
&nbsp;&nbsp;&nbsp;&nbsp;techId  
&nbsp;&nbsp;&nbsp;&nbsp;techName  
&nbsp;&nbsp;&nbsp;&nbsp;techType  
&nbsp;&nbsp;&nbsp;&nbsp;users: Seq[skillMatrixUsersAndLevel] // maybe the name is not that good, but no inspiration has come to us

*SkillMatrixUsersAndLevel*:  
&nbsp;&nbsp;&nbsp;&nbsp;userName // this is the first and last name combined together  
&nbsp;&nbsp;&nbsp;&nbsp;level

This looks like this:
```json
{
      "techId": 7,
      "techName": "Defense Against the Dark Arts",
      "techType": "CONCEPT",
      "users": [
        {
          "userName": "Severus Snape",
          "level": "EXPERT"
        },
         {
           "userName": "Dolores Umbridge",
            "level": "NOVICE"
         }
      ]
}
```

UserSkillResponse:
    userId
    firstName
    lastName
    skills: Seq[SkillMatrixItem]

```json
{
    "userId": 4,
    "firstName": "Severus",
    "lastName": "Snape",
    "skill": [
      {
        "tech": {
          "id": 7,
          "name": "Dark Arts",
          "techType": "CONCEPT"
        },
        "skillLevel": "EXPERT"
      },
      {
        "tech": {
          "id": 8,
          "name": "Defense against the Dark Arts",
          "techType": "CONCEPT"
        },
        "skillLevel": "EXPERT"
      }
    ]
}
```
# Initial Configuration
The configuration of the project goes under conf/application.conf  
```<code>
Right now, we are using PostgreSQL, and you should change the properties 'url', 'user' and 'password' according to the configuration of your local database
```
# TO DO:
- add documentation (comments)
- generate db schema

### Good to have:
- logging
- jenkins build
- if you have ideas on how to improve the code, feel free to do it!
- new features

# Have ideas on adding or changing functionality?
Talk to the product owner, Erik Bakker