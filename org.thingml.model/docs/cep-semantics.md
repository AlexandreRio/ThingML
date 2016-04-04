# Complex Event Processing Semantics in ThingML

## Join Sources

### With time window

```ruby
stream joinStream @TTL "500"
from join: [t: rcvP?temp & p: rcvP?pressure -> cep()]::during 5000 by 1000
produce sendP!cep()
```

`@TTL` is optional, default value is `250`, it means that `temp` and `pressure` events are joinable
if they are received in a `500`ms time window.

The step of this stream is `1000`ms, meaning that every `1000`ms the output events, `cep` may be created
if the requirements are meant (`temp` and `pressure` events in a `500`ms time interval).

The output messages, `cep` will be saved for `5000`ms.

### With length window

Length window allow to store a particular number of output messages, here `cep`. Once the buffer is full the number of message to remove is specified by the step of the window.

```ruby
stream joinStream @TTL "500"
from join: [t: rcvP?temp & p: rcvP?pressure -> cep()]::buffer 5 by 2
produce sendP!cep()
```

In this exemple the two oldest `cep` messages are removed from the buffer every time it reached the size of `5` messages.

### Without any window

```ruby
stream joinStream
from join: [t: rcvP?temp & p: rcvP?pressure -> cep()]
produce sendP!cep()
```

In that case we do not want to store the output event `cep`. We store only the last message for every input events, here `temp` and `pressure`.

Every time an input message is received we check if it is joinable with the other one, meaning both are in the same time frame defined by the `TTL` in this case the default one: `250`ms.

We garanty this property over the reception time of messages.

![equation](https://raw.githubusercontent.com/AlexandreRio/ThingML/master/org.thingml.model/docs/join_time_property.png)

### Adding guards

Guards can be added to specify constaints over input and output messages in streams. For instance to filter input values you can do as follow:

```ruby
stream joinStream
from join: [t: rcvP?temp::keep if t.value > 10 &
            p: rcvP?pressure
            -> cep()
            ]::keep if true
produce sendP!cep()
```

### Event consumption policy

Considering this stream:

```ruby
stream joinStream @TTL "2000"
from join: [t: rcvP?temp & p: rcvP?pressure -> cep()]::during 1000 by 1000
produce sendP!cep()
```

If we suppose that `temp` messages are produced twice as often as `pressure` messages we may want to join the same `pressure` message with two different `temp` messages (considering their `TTL` is still valid).

You can prevent this behavior by adding an annotation to a message or to a stream, impacting every input messages, as shown in the example bellow:

```ruby
stream joinStream @TTL "2000"
from join: [t: rcvP?temp &
            p: rcvP?pressure @UseOnce "True"
            -> cep()
           ]::during 1000 by 1000
produce sendP!cep()
```

or for all messages:

```ruby
stream joinStream @TTL "2000" @UseOnce "True"
from join: [t: rcvP?temp & p: rcvP?pressure -> cep()]::during 1000 by 1000
produce sendP!cep()
```

### Input buffers

The default behavior is to store only the last message for every input events.

Considering this stream joining temperature and pressure into a new message `simple_joined`
containing both:

```ruby
stream joinStream
from join: [t: rcvP?temp & p: rcvP?pressure -> simple_joined(t.v, p.v)]
produce sendP!simple_joined(t.v, p.v)
```

If we now consider the following message sequence:

```c
t1 (22), p1 (1033), t2( 23), p2 (1024)
```

If every messages is emitted in the same interval smaller than the `TTL` the output will be the following:

```c
simple_joined(23, 1033), simple_joined(23, 1024)
```

To obtain `simple_joined(23, 1033), simple_joined(22, 1024)` you have to use both `@UseOnce` and `@Buffer` annotation, such as:

```ruby
stream joinStream @UseOnce "True" @Buffer "5"
from join: [t: rcvP?temp & p: rcvP?pressure -> simple_joined(t.v, p.v)]
produce sendP!simple_joined(t.v, p.v)
```

## Merge Sources

Merge streams work the same way as join streams except they work as a logical `OR` instead of a logical `AND`, meaning only one input event is needed to produce an output event. The syntax is as follow:

```ruby
stream mergeStream
from m: [ e1: sensor1?temp | e2: sensor2?temp | e3: sensor3?temp -> res]
produce sendP!res(m.value)
```

### Window and guards

Like join streams, merge streams allow the same kind of time and length window and guards having the same effects.

## Simple Sources

Simple source streams only wait for one event message, a basic syntax using a `select` statement calling a previously defined funcion is as follow:

```ruby
stream simpleStream
from s: rcvP?temp
select var a: Int = transformValue(s.value)
produce sendP!res(a)
```

### Window and guards

As every other stream simple source stream allow time and length window and also guards.