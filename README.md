# gladtask API

### Get user logged information in the back side?

In  any controller's method if you add a paramenter ``HttpServletRequest` it will comes with a lot of fun info from the HTTP REQUEST.
for instance, if you want to get user's request information for whatever reason, you know that we, gladtask, controls security via JWT, so in every (or at least almost) request
from a client the token is sent via **http request's header**, more specifically *Authorization* name variable.

Joining the dots, we have the HttpServletRequest in one hand (which already passed by spring security's filter), in another we have the amazing [JwtTokenUtil.java](https://gitlab.com/denisneres/gladtask/blob/develop/gladtask/src/main/java/br/com/glad/gladtask/security/jwt/JwtTokenUtil.java) that
does any kind of extration with gladtask's token... So, we just need:

```
String authToken = httpServletRequest.getHeader("Authorization");
String usernameFromToken = jwtUtil.getUsernameFromToken(authToken);
````
and voile, we have user logged's username. 




A task management made by glads.
