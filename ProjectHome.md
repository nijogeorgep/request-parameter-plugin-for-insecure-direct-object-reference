https://cwiki.apache.org/S2PLUGINS/request-parameter-to-action-object-mapping-plugin-for-insecure-direct-object-references.html

JSP Parameter to Action Object Mapping (Security) Plugin does this great thing. Here is also a short overview of what it does and why a developer would want to use it.

Many applications expose their internal object references to users. Attackers use parameter tampering to change references and violate the intended but unenforced access control policy. Frequently, these references point to file systems and databases, but any exposed application construct could be vulnerable.

The best protection is to avoid exposing direct object references to users by using an index, indirect reference map, or other indirect method that is easy to validate. If a direct object reference must be used, ensure that the user is authorized before using it.

  * Avoid exposing your private object references to users whenever possible, such as primary keys or filenames
  * Validate any private object references extensively with an "accept known good" approach
  * Verify authorization to all referenced objects

So to avoid internal object implementation to end user, this plugin can be used.