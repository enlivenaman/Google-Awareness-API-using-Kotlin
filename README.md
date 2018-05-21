# Google-Awareness-API-using-Kotlin
Awareness API allows you to get data based on user's context easier, more accurate with better performance.

Context Types
There are 7 kinds of context that we can work with in Awareness API such as

Time - Local time at current user's location

Location - Latitude/Longitude

Place - Places around user

Activity - Detected user activity (biking, walking, running, etc.)

Beacons - Check nearby beacon(s)

Headphones - Are headphones plugged in?

Weather - Current weather conditions

There are two sets of API available in Awareness API such as

Snapshot API - Allows you to "request an information based on user's context" as listed above.

Fence API - Allows you to "receive a signal when user's context has changed and reaches the condition" through callback function, for example, if user moves closed to the specific coordinate with headphones plugged in, Fench API will call the registered BroadcastReceiver and let you do your job.

Actually those two sets of API work with the exact same set of data. Google just seperates it into two to make it easier to communicate. Snapshot to get data, Fence to detect change. That's all.
