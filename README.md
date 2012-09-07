# tapestry-profiler

tapestry-profiler is a simple, configurable, sampling profiler designed to be used in production
Tapestry 5 applications to track down performance issues. Overhead is minimal and it has been battle
tested on large, busy Tapestry applications.

Rather than spend all kinds of time replicating the production database, reproducing the slow behavior
(not always possible), and connecting an expensive profiler to it, why not just get a sampling of
what's going on when the action is hot? That's the philosophy of this tool. It ain't perfect, and
you won't get invocation counts, but it will give you something to go on.

# Download

The latest jar can be retrieved through Maven:

```
server: http://widen.artifactoryonline.com/widen/libs-widen-public
group: com.widen
artifact: profiler
version: 1.0-SNAPSHOT
```

Or by browsing the repo directly at https://widen.artifactoryonline.com/widen/libs-widen-public

# Controls & Screenshots

A simple control is included that can be dropped onto a page (probably some internal page or component).
Alternatively, you can create your own control that manipulates the ProfilerState SSO directly.

![Control Screenshot](https://raw.github.com/bdotte/tapestry-profiler/master/control-screenshot.png)

The Start Profiling button will kick off a thread that samples all running threads every 10ms (by default),
and aggregates that data together as it goes. The button turns into a __Capture Snapshot__ button (via Zone update)
and remains that way, even if you go to other pages, until it is clicked.

Once you click __Capture Snapshot__, a results page is displayed (css included):

![Results Screenshot](https://raw.github.com/bdotte/tapestry-profiler/master/results-screenshot.png)

You'll notice from the screenshot that we take advantage of certain assumptions that can be made about
Tapestry applications. Since all pages are always in a *pages* package, we can associate those threads
with the appropriate page and report on those separately. We also go on the assumption that packages
with *components* are Tapestry components to color-code those lines. (The definition of a *DAO* is
configurable.)

From this screenshot, 2 hotspots pop out: DAOImpl.getRelevantCases() and Layout.getTitle(). The
__Overall__ section is often the most useful, but if there is lots of concurrent use, it isn't always
clear which lines belong to which requests, because they are all combined together. The key is to look
for the "cliffs", in this case Board.setupRender() follows into DAOImpl.getRelevantCases(), which turns
out to be the root problem of that section. Layout.getTitle() is an isolated, separate, problem.

# Configuration

Configuration is accomplished through the symbols in ProfilerSymbols.java, and some overrideable services
bound in ProfilerModule.java.

* __ProfilerSymbols.APPLICATION_PACKAGE__ Your application's base package. Anything else falls under the
"Uninteresting Lines" toggle on the results page.
* __ProfilerSymbols.MONITOR_JOBS__ If your application has jobs running (Quartz for example), enable this
to track jobs separately (similar to how pages are tracked separately).
* __ProfilerSymbols.POLLING_INTERVAL__ The interval between polling all threads. Defaults to 10ms.
* __ProfilerSymbols.MAX_THREAD_RUNTIME__ The maximum amount of time the profiler thread is allowed to run.
Defaults to 60 minutes.
* __ProfilerSymbols.IGNORED_STACK_STRINGS__ A space-delimited list of strings you want to completely ignore
when they appear (java. sun. etc.)

Services that can be overridden:

* __JobIdentifier__ Use this to define which classes are considered jobs in your application, so they can
be tracked separately. By default, no classes are considered jobs.
* __DAOIdentifier__ Defines which classes are considered DAOs, so they can be color-coded in the results.
By default, anything with "dao" anywhere in the fully qualified name is considered a DAO.
* __ProfilerAccessController__ Since the profiler is designed to be run in a production environment,
security considerations come into play. Please override this service so that you can deny non-superusers
access to the results page.

# Contact & License

Contact Ben Dotte (bdotte at widen.com) with questions.

Licensed under Apache, Version 2.0.