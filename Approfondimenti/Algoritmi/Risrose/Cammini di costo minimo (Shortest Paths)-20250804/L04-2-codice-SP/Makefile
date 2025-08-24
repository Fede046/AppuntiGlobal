JAVA_FILES := $(wildcard *.java)
JAVA_CLASSES := $(patsubst %.java, %.class, $(JAVA_FILES))

ALL: $(JAVA_CLASSES)

.PHONY: clean

%.class: %.java
	javac $?

clean:
	\rm -f *.class *~
