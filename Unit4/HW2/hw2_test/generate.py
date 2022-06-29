#!/usr/bin/env python3
import os
import random
import subprocess

ids = []
classIds = []
interfaceIds = []
attrIds = []
names = []
classNames = []
interfaceNames = []
attrNames = []
opNames = []
interactionNames = []
collaborationNames = []
lifelineNames = []
endponitId = []
stateMachineNames = []
datas = []
asso = []
classOp = []
classAttr = []
interactionLifeline = []
stateMachineState = []
smSt = {}
intlife = {}


def createId():
    global ids
    _id = ""
    while True:
        _id = "AAAAAA" + \
              "".join(random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3, 13)) + \
              "="
        if _id not in ids:
            break
    ids.append(_id)
    return _id


def createVisibility():
    n = random.randint(1, 6)
    if n == 1:
        return "public"
    elif n == 2:
        return "protected"
    elif n == 3 or n >= 5:
        return "private"
    elif n == 4:
        return "package"


def createAggregation():
    n = random.randint(1, 3)
    if n == 1:
        return "none"
    if n == 2:
        return "shared"
    if n == 3:
        return "composite"


def createName():
    global names
    r = random.random()
    name = ""
    _len = len(names)
    if r < 0.1 and _len != 0:
        name = names[random.randint(0, _len - 1)]
    else:
        while True:
            name = method_name_1()
            if name not in names:
                names.append(name)
                break
    return name


def createClassName():
    global names
    global classNames
    r = random.random()
    className = ""
    _len = len(classNames)
    if r < 0.1 and _len != 0:
        className = classNames[random.randint(0, _len - 1)]
    else:
        while True:
            className = method_name_1()
            if className not in classNames:
                classNames.append(className)
                if className not in names:
                    names.append(className)
                break
    return className


def createInterfaceName():
    global names
    global interfaceNames
    r = random.random()
    interfaceName = ""
    _len = len(interfaceNames)
    if r < 0.1 and _len != 0:
        interfaceName = interfaceNames[random.randint(0, _len - 1)]
    else:
        while True:
            interfaceName = "".join(random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZ', 1)) + \
                            "".join(random.sample('abcdefghijklmnopqrstuvwxyz' * 3, random.randint(2, 5)))
            if interfaceName not in interfaceNames:
                interfaceNames.append(interfaceName)
                if interfaceName not in names:
                    names.append(interfaceName)
                break
    return interfaceName


def createAttrName():
    global names
    global attrNames
    r = random.random()
    attrName = ""
    _len = len(attrNames)
    if r < 0.1 and _len != 0:
        attrName = attrNames[random.randint(0, _len - 1)]
    else:
        while True:
            attrName = method_name_1()
            if attrName not in attrNames:
                attrNames.append(attrName)
                if attrName not in names:
                    names.append(attrName)
                break
    return attrName


def createOpName():
    global names
    global opNames
    r = random.random()
    opName = ""
    _len = len(opNames)
    if r < 0.1 and _len != 0:
        opName = opNames[random.randint(0, _len - 1)]
    else:
        while True:
            opName = method_name_1()
            if opName not in opNames:
                opNames.append(opName)
                if opName not in names:
                    names.append(opName)
                break
    return opName


def createInteractionName():
    global names
    global interactionNames
    r = random.random()
    interactionName = ""
    _len = len(interactionName)
    if r < 0.1 and _len != 0:
        interactionName = interactionNames[random.randint(0, _len - 1)]
    else:
        while True:
            interactionName = method_name_1()
            if interactionName not in interactionNames:
                interactionNames.append(interactionName)
                if interactionName not in names:
                    names.append(interactionName)
                break
    return interactionName


def createCollaborationName():
    global names
    global collaborationNames
    r = random.random()
    collaborationName = ""
    _len = len(collaborationName)
    if r < 0.1 and _len != 0:
        collaborationName = collaborationNames[random.randint(0, _len - 1)]
    else:
        while True:
            collaborationName = method_name_1()
            if collaborationName not in collaborationNames:
                collaborationNames.append(collaborationName)
                if collaborationName not in names:
                    names.append(collaborationName)
                break
    return collaborationName


def createStateMachineName():
    global names
    global stateMachineNames
    r = random.random()
    stateMachineName = ""
    _len = len(stateMachineName)
    if r < 0.1 and _len != 0:
        stateMachineName = stateMachineNames[random.randint(0, _len - 1)]
    else:
        while True:
            stateMachineName = method_name_1()
            if stateMachineName not in stateMachineNames:
                stateMachineNames.append(stateMachineName)
                if stateMachineName not in stateMachineNames:
                    names.append(stateMachineName)
                break
    return stateMachineName


def createType():
    global classNames
    r = random.randint(0, 100)
    if (r < 90):
        return random.sample(['int', 'double', 'long', 'boolean', 'String', 'byte'], 1)[0]
    else:
        return random.sample(['ArrayList', 'HashSet', 'HashMap'], 1)[0]


def dic2Str(_dict):
    _str = str(_dict)
    _str = _str.replace(' ', '')
    _str = _str.replace('\'', '\"')
    _str = _str.replace("None", "null")
    _str = _str.replace("\"false\"", "false")
    _str = _str.replace("\"true\"", "true")
    return _str


classParent = createId()
interfaceParent = createId()
collaborationParent = createId()


def createAssociation(_id, end1Id, end2Id, class1Id):
    global datas
    info = {}
    info["_parent"] = class1Id
    info["name"] = "AssociationFrom" + class1Id[:10]
    info["_type"] = "UMLAssociation"
    info["end2"] = end2Id
    info["end1"] = end1Id
    info["_id"] = createId()
    datas.append(dic2Str(info))
    return


def createAssociationEnd():
    global classIds
    global interfaceIds
    global datas
    totalIds = classIds + interfaceIds
    info1 = {}
    info2 = {}
    _parent = createId()
    for i in range(1, 3):
        info = {}
        if i == 1:
            info = info1
        else:
            info = info2
        info["reference"] = str(random.sample(totalIds, 1)[0])
        info["multiplicity"] = ""
        info["_parent"] = _parent
        info["visibility"] = createVisibility()
        info["name"] = createName()
        info["_type"] = "UMLAssociationEnd"
        info["aggregation"] = createAggregation()
        info["_id"] = createId()
    datas.append(dic2Str(info1))
    datas.append(dic2Str(info2))
    asso.append((info1["reference"], info2["reference"]))
    createAssociation(_parent, info1["_id"], info2["_id"], info1["reference"])
    return


def createAttribute(parentId, className):
    global attrIds
    global classAttr
    global datas
    partAttrName = []
    n = random.randint(0, 10)
    if n == 0:
        return
    for i in range(n):
        attrName = createAttrName()
        if attrName in partAttrName:
            continue
        partAttrName.append(attrName)
        info = {}
        info["_parent"] = parentId
        info["visibility"] = createVisibility()
        info["name"] = attrName
        info["_type"] = "UMLAttribute"
        info["_id"] = createId()
        info["type"] = createType()
        attrIds.append(info["_id"])
        datas.append(dic2Str(info))
        classAttr.append((className, attrName))
    return


def createClass():
    global classParent
    global classIds
    global datas
    _id = createId()
    info = {}
    info["_parent"] = classParent
    info["visibility"] = createVisibility()
    info["name"] = createClassName()
    info["_type"] = "UMLClass"
    info["_id"] = _id
    classIds.append(_id)
    datas.append(dic2Str(info))
    createAttribute(_id, info['name'])
    createOperation(_id, info["name"])
    r = random.random()
    if r < 0.3:
        createStateMachine(_id)
    return


def createStateMachine(parentId):
    global datas
    info = {}
    info["_parent"] = parentId
    info["name"] = createStateMachineName()
    info["_type"] = "UMLStateMachine"
    info["_id"] = createId()
    datas.append(dic2Str(info))
    createRegion(info["_id"], info["name"])
    return


def createRegion(parentId, parentName):
    global datas
    info = {}
    info["_parent"] = parentId
    info["visibility"] = createVisibility()
    info["name"] = parentName + "1"
    info["_type"] = "UMLRegion"
    info["_id"] = createId()
    datas.append(dic2Str(info))
    createState(info["_id"], parentName)
    return


def createState(parentId, parentName):
    global stateMachineState
    global datas
    state = {}
    r = random.random()
    info = {}
    info["_parent"] = parentId
    info["visibility"] = createVisibility()
    info["name"] = None
    info["_type"] = "UMLPseudostate"
    info["_id"] = createId()
    state["pseudo"] = info["_id"]
    stateMachineState.append([parentName, info["name"]])
    datas.append(dic2Str(info))
    r = random.random()
    if r < 0.3:
        info = {}
        info["_parent"] = parentId
        info["visibility"] = createVisibility()
        info["name"] = None
        info["_type"] = "UMLFinalState"
        info["_id"] = createId()
        state["final"] = info["_id"]
        stateMachineState.append([parentName, info["name"]])
        datas.append(dic2Str(info))
    normalState = []
    partName = []
    n = random.randint(0, 10)
    if n != 0:
        for i in range(n):
            info = {}
            info["_parent"] = parentId
            info["visibility"] = createVisibility()
            while True:
                name = createName()
                if name not in partName:
                    partName.append(name)
                    info["name"] = name
                    break
            info["_type"] = "UMLState"
            info["_id"] = createId()
            normalState.append(info["_id"])
            stateMachineState.append([parentName, info["name"]])
            smSt.get(parentName, []).append(info["name"])
            datas.append(dic2Str(info))
    state["normal"] = normalState
    createTransition(parentId, state)
    return


def createTransition(parentId, state):
    global datas
    normalState = state["normal"]
    for state1 in normalState:
        for state2 in normalState:
            r = random.random()
            if r < 0.5:
                n = random.randint(1, 3)
                for i in range(n):
                    info = {}
                    info["_parent"] = parentId
                    info["visibility"] = createVisibility()
                    info["guard"] = "".join(
                        random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3,
                                      random.randint(3, 5)))
                    info["name"] = "".join(
                        random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3,
                                      random.randint(3, 5)))
                    info["_type"] = "UMLTransition"
                    info["_id"] = createId()
                    info["source"] = state1
                    info["target"] = state2
                    createEvent(info["_id"])
                    datas.append(dic2Str(info))
    if state.__contains__("pseudo"):
        state1 = state["pseudo"]
        for state2 in normalState:
            r = random.random()
            if r < 0.5:
                n = random.randint(1, 3)
                for i in range(n):
                    info = {}
                    info["_parent"] = parentId
                    info["visibility"] = createVisibility()
                    info["guard"] = "".join(
                        random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3,
                                      random.randint(3, 5)))
                    info["name"] = "".join(
                        random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3,
                                      random.randint(3, 5)))
                    info["_type"] = "UMLTransition"
                    info["_id"] = createId()
                    info["source"] = state1
                    info["target"] = state2
                    createEvent(info["_id"])
                    datas.append(dic2Str(info))
    if state.__contains__("final"):
        state2 = state["final"]
        for state1 in normalState:
            r = random.random()
            if r < 0.5:
                n = random.randint(1, 3)
                for i in range(n):
                    info = {}
                    info["_parent"] = parentId
                    info["visibility"] = createVisibility()
                    info["guard"] = "".join(
                        random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3,
                                      random.randint(3, 5)))
                    info["name"] = "".join(
                        random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3,
                                      random.randint(3, 5)))
                    info["_type"] = "UMLTransition"
                    info["_id"] = createId()
                    info["source"] = state1
                    info["target"] = state2
                    createEvent(info["_id"])
                    datas.append(dic2Str(info))
    return


# {"_parent":"AAAAAAF8grB2N6WbWC8=","expression":null,"visibility":"public","name":"a","_type":"UMLEvent","_id":"AAAAAAF8grB7+qWtpag=","value":null}
def createEvent(parentId):
    global datas
    info = {}
    info["_parent"] = parentId
    info["_expression"] = None
    info["visibility"] = createVisibility()
    info["name"] = "".join(
        random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456879' * 3,
                      random.randint(3, 5)))
    info["_type"] = "UMLEvent"
    info["_id"] = createId()
    info["_value"] = None
    datas.append(dic2Str(info))
    return


def createGeneralization():
    global classIds
    global interfaceIds
    global datas

    n = random.randint(2, 6)
    i = 0
    classLen = len(classIds)
    while n <= classLen - i:
        objectClasses = classIds[i:i + n]
        for j in range(n - 1):
            info = {}
            info["_parent"] = objectClasses[j]
            info["name"] = createName()
            info["_type"] = "UMLGeneralization"
            info["_id"] = createId()
            info["source"] = objectClasses[j]
            info["target"] = objectClasses[j + 1]
            datas.append(dic2Str(info))
        i = i + n
        n = random.randint(2, 6)

    n = random.randint(2, 12)
    i = 0
    interfaceLen = len(interfaceIds)
    while n <= interfaceLen - i:
        interfaceClasses = interfaceIds[i:i + n]
        tn = random.randint(2, 4)
        ti = 0
        splits = []
        while tn <= n - 1 - ti:
            split = interfaceClasses[ti:ti + tn]
            splits.append(split)
            ti = ti + tn
            tn = random.randint(2, 4)
        splitNum = len(splits)
        if splitNum >= 2:
            for j in range(splitNum - 1):
                split1 = splits[j]
                split2 = splits[j + 1]
                _len = len(split2)
                for source in split1:
                    targets = random.sample(split2, random.randint(1, _len))
                    for target in targets:
                        info = {}
                        info["_parent"] = source
                        info["name"] = createName()
                        info["_type"] = "UMLGeneralization"
                        info["_id"] = createId()
                        info["source"] = source
                        info["target"] = target
                        datas.append(dic2Str(info))
        i = i + n
        n = random.randint(2, 6)
    return


def createInterface():
    global interfaceParent
    global interfaceIds
    global datas
    _id = createId()
    info = {}
    info["_parent"] = interfaceParent
    info["visibility"] = createVisibility()
    info["name"] = createInterfaceName()
    info["_type"] = "UMLInterface"
    info["_id"] = _id
    interfaceIds.append(_id)
    datas.append(dic2Str(info))
    return


def createInterfaceRealization():
    global classIds
    global interfaceIds
    global datas
    classIdSet = set(classIds)
    interfaceIdSet = set(interfaceIds)
    _len = len(interfaceIdSet)
    for classId in classIdSet:
        n = random.randint(1, _len)
        r = random.random()
        if r < 0.2:
            n = 0
        implements = random.sample(list(interfaceIdSet), n)
        for implement in implements:
            info = {}
            info["_parent"] = classId
            info["name"] = createName()
            info["_type"] = "UMLInterfaceRealization"
            info["_id"] = createId()
            info["source"] = classId
            info["target"] = implement
            datas.append(dic2Str(info))
    return


'''def createOperationInterface(parentId):
    global classOp
    global datas
    n = random.randint(0, 10)
    if n == 0:
        return
    for i in range(n):
        _id = createId()
        info = {}
        info["_parent"] = parentId
        info["visibility"] = createVisibility()
        info["name"] = createOpName()
        info["_type"] = "UMLOperation"
        info["_id"] = _id
        datas.append(dic2Str(info))
        createParameter(_id)
    return'''


def createOperation(parentId, className):
    global classOp
    global datas
    n = random.randint(0, 10)
    if n == 0:
        return
    for i in range(n):
        _id = createId()
        info = {}
        info["_parent"] = parentId
        info["visibility"] = createVisibility()
        info["name"] = createOpName()
        info["_type"] = "UMLOperation"
        info["_id"] = _id
        datas.append(dic2Str(info))
        classOp.append((className, info["name"]))
        createParameter(_id)
    return


def createParameter(parentId):
    global datas
    n = random.randint(0, 5)
    if n == 0:
        return
    for i in range(n):
        info = {}
        info["_parent"] = parentId
        info["name"] = "parameter" + str(i)
        info["_type"] = "UMLParameter"
        info["_id"] = createId()
        info["type"] = createType()
        info["direction"] = "in"
        if i == n - 1:
            r = random.randint(1, 3)
            if r != 1:
                info["direction"] = "return"
        datas.append(dic2Str(info))
    return


def createCollaboration():
    global datas
    info = {}
    info["_parent"] = collaborationParent
    info["name"] = createCollaborationName()
    info["_type"] = "UMLCollaboration"
    info["_id"] = createId()
    createInteraction(info["_id"])
    datas.append(dic2Str(info))


def createInteraction(patrentId):
    global interactionParent
    global datas
    info = {}
    info["_parent"] = patrentId
    info["name"] = createInteractionName()
    info["_type"] = "UMLInteraction"
    info["_id"] = createId()
    createLifeline(info["_id"], info["name"])
    datas.append(dic2Str(info))
    return


def createLifeline(parentId, parentName):
    global attrIds
    global lifelineNames
    global interactionLifeline
    global datas
    n = random.randint(0, 10)
    partName = []
    lifelineIds = []
    if n != 0:
        for i in range(n):
            info = {}
            info["_parent"] = parentId
            info["visibility"] = createVisibility()
            name = createName()
            r = random.random()
            if r < 0.1 and len(partName) >= 1:
                name = random.sample(partName, 1)[0]
            else:
                partName.append(name)
            info["name"] = name
            info["_type"] = "UMLLifeline"
            info["isMultiInstance"] = random.sample(["true", "false"], 1)[0]
            info["_id"] = createId()
            if len(attrIds) > 0:
                info["represent"] = random.sample(attrIds, 1)[0]
            else:
                info["represent"] = createId()
            datas.append(dic2Str(info))
            lifelineIds.append(info["_id"])
            lifelineNames.append(info["name"])
            interactionLifeline.append([parentName, name])
            intlife.get(parentName, []).append(info["name"])
    n = random.randint(0, 3)
    partName1 = []
    if n != 0:
        for i in range(n):
            info = {}
            info["_parent"] = parentId
            info["visibility"] = createVisibility()
            name = createName()
            r = random.random()
            if r < 0.1 and len(partName1) >= 1:
                name = random.sample(partName1, 1)[0]
            else:
                partName1.append(name)
            info["name"] = name
            info["_type"] = "UMLEndpoint"
            info["_id"] = createId()
            datas.append(dic2Str(info))
            endponitId.append(info["_id"])
            lifelineIds.append(info["_id"])
    createMessage(lifelineIds, parentId)
    return


def createMessage(lifelineIds, parentId):
    global datas
    for id1 in lifelineIds:
        for id2 in lifelineIds:
            n = 0
            r = random.random()
            if r < 0.5:
                n = random.randint(1, 3)
            if n != 0:
                for i in range(n):
                    info = {}
                    info["messageSort"] = \
                        random.sample(["synchCall", "reply", "createMessage"], 1)[0]
                    info["_parent"] = parentId
                    info["visibility"] = createVisibility()
                    info["name"] = "Message" + str(i)
                    info["_type"] = "UMLMessage"
                    info["_id"] = createId()
                    info["source"] = id1
                    if id1 in endponitId and info["messageSort"] == "createMessage":
                        continue
                    info["target"] = id2
                    datas.append(dic2Str(info))
    return


def main():
    n = random.randint(10, 20)
    for i in range(n):
        createClass()
    n = random.randint(10, 40)
    for i in range(n):
        createInterface()
    n = random.randint(10, 100)
    for i in range(n):
        createAssociationEnd()
    createInterfaceRealization()
    createGeneralization()
    n = random.randint(1, 10)
    for i in range(n):
        createCollaboration()


def data2Stdin():
    stdin = open("in.txt", "w")
    random.shuffle(datas)
    for data in datas:
        stdin.write(data + "\n")
    stdin.write("END_OF_MODEL\n")
    for i in range(1000):
        r = random.random()
        n = random.randint(1, 6)
        ret = instrDic[n]()
        stdin.write(ret + '\n')

    stdin.write(stateCount1("1208B") + '\n')
    for name in stateMachineNames:
        stdin.write(stateCount1(name) + '\n')
    for name in stateMachineNames:
        for state_name in smSt.get(name, []):
            stdin.write(stateCriticalPoint1(name, state_name) + '\n')
        for state_name1 in smSt.get(name, []):
            for state_name2 in smSt.get(name, []):
                stdin.write(trasitionTrigger1(name, state_name1, state_name2) + '\n')
    for name in interactionNames:
        stdin.write(ptcpObjCount1(name) + '\n')
    for name in interactionNames:
        for lifeName in intlife.get(name, []):
            stdin.write(ptcpCreator1(name, lifeName) + '\n')
        for lifeName in intlife.get(name, []):
            stdin.write(ptcpLostAndFound1(name, lifeName) + '\n')
    stdin.close()


def method_name_1():
    return "".join(random.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZ', 1)) + \
           "".join(random.sample('abcdefghijklmnopqrstuvwxyz' * 3, random.randint(2, 5)))


def createOperationQuery():
    types = ["NON_RETURN", "RETURN", "NON_PARAM", "PARAM"]
    ret = " ".join(random.sample(types, random.randint(0, 4)))
    return ret


def createMode():
    types = ["ALL", "SELF_ONLY"]
    ret = str(random.sample(types, 1)[0])
    return ret


def classCount():
    return "CLASS_COUNT"


def classOperationCount():
    global classNames
    r = random.random()
    name = str(random.sample(classNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "CLASS_OPERATION_COUNT " + name


def classAttrCount():
    global classNames
    r = random.random()
    name = str(random.sample(classNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "CLASS_ATTR_COUNT " + name + " " + createMode()


def classAssoCount():
    global classNames
    r = random.random()
    name = str(random.sample(classNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "CLASS_ASSO_COUNT " + name


def classAssoClassList():
    global classNames
    r = random.random()
    name = str(random.sample(classNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "CLASS_ASSO_CLASS_LIST " + name


def classOperationVisibility():
    global classOp
    global opNames
    classAndOp = random.sample(classOp, 1)[0]
    className = classAndOp[0]
    opName = classAndOp[1]
    r = random.random()
    if r < 0.1:
        className = method_name_1()
    if r < 0.05 or r > 0.95:
        opName = method_name_1()
    if 0.85 < r <= 0.95:
        opName = random.sample(opNames, 1)[0]
    return "CLASS_OPERATION_VISIBILITY " + className + " " + opName


def classAttrVisibility():
    global classAttr
    global attrNames
    classAndAttr = random.sample(classAttr, 1)[0]
    className = classAndAttr[0]
    attrName = classAndAttr[1]
    r = random.random()
    if r < 0.1:
        className = method_name_1()
    if r < 0.05 or r > 0.95:
        attrName = method_name_1()
    if 0.85 < r <= 0.95:
        attrName = random.sample(attrNames, 1)[0]
    return "CLASS_ATTR_VISIBILITY " + className + " " + attrName


def classTopBase():
    global classNames
    r = random.random()
    name = str(random.sample(classNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "CLASS_TOP_BASE " + name


def classImplementInterfaceList():
    global classNames
    r = random.random()
    name = str(random.sample(classNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "CLASS_IMPLEMENT_INTERFACE_LIST " + name


def classInfoHidden():
    global classNames
    r = random.random()
    name = str(random.sample(classNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "CLASS_INFO_HIDDEN " + name


def ptcpObjCount():
    global interactionNames
    r = random.random()
    name = str(random.sample(interactionNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "PTCP_OBJ_COUNT " + name


def ptcpObjCount1(s):
    return "PTCP_OBJ_COUNT " + s


def ptcp_creator():
    global interactionLifeline
    if len(interactionLifeline) == 0:
        interactionName = method_name_1()
        lifelineName = method_name_1()
    else:
        interactionAndLifeline = random.sample(interactionLifeline, 1)[0]
        interactionName = interactionAndLifeline[0]
        lifelineName = interactionAndLifeline[1]
        r = random.random()
        if r < 0.1:
            interactionName = method_name_1()
        if r < 0.05 or r > 0.95:
            lifelineName = method_name_1()
        if 0.85 < r <= 0.95:
            lifelineName = random.sample(lifelineNames, 1)[0]
    return "PTCP_CREATOR " + interactionName + " " + lifelineName


def ptcpCreator1(s1, s2):
    return "PTCP_CREATOR " + s1 + " " + s2


def ptcpLostAndFound1(s1, s2):
    return "PTCP_LOST_AND_FOUND " + s1 + " " + s2


def ptcp_lost_and_found():
    global interactionLifeline
    if len(interactionLifeline) == 0:
        interactionName = method_name_1()
        lifelineName = method_name_1()
    else:
        interactionAndLifeline = random.sample(interactionLifeline, 1)[0]
        interactionName = interactionAndLifeline[0]
        lifelineName = interactionAndLifeline[1]
        r = random.random()
        if r < 0.1:
            interactionName = method_name_1()
        if r < 0.05 or r > 0.95:
            lifelineName = method_name_1()
        if 0.85 < r <= 0.95:
            lifelineName = random.sample(lifelineNames, 1)[0]
    return "PTCP_LOST_AND_FOUND " + interactionName + " " + lifelineName


def messageCount():
    global interactionNames
    r = random.random()
    name = str(random.sample(interactionNames, 1)[0])
    if r < 0.1:
        name = method_name_1()
    return "MESSAGE_COUNT " + name


def incomingMsgCount():
    global interactionLifeline
    if len(interactionLifeline) == 0:
        interactionName = method_name_1()
        lifelineName = method_name_1()
    else:
        interactionAndLifeline = random.sample(interactionLifeline, 1)[0]
        interactionName = interactionAndLifeline[0]
        lifelineName = interactionAndLifeline[1]
        r = random.random()
        if r < 0.1:
            interactionName = method_name_1()
        if r < 0.05 or r > 0.95:
            lifelineName = method_name_1()
        if 0.85 < r <= 0.95:
            lifelineName = random.sample(lifelineNames, 1)[0]
    return "INCOMING_MSG_COUNT " + interactionName + " " + lifelineName


def stateCount():
    global stateMachineNames
    name = ""
    if len(stateMachineNames) == 0:
        name = method_name_1()
    else:
        r = random.random()
        name = str(random.sample(stateMachineNames, 1)[0])
        if r < 0.1:
            name = method_name_1()
    return "STATE_COUNT " + name


def transitionCount():
    global stateMachineNames
    if len(stateMachineNames) == 0:
        name = method_name_1()
    else:
        r = random.random()
        name = str(random.sample(stateMachineNames, 1)[0])
        if r < 0.1:
            name = method_name_1()
    return "TRANSITION_COUNT " + name


def subsequentStateCount():
    global stateMachineState
    if len(stateMachineState) == 0:
        stateMachineName = method_name_1()
        stateName = method_name_1()
    else:
        stateMachineAndState = random.sample(stateMachineState, 1)[0]
        stateMachineName = stateMachineAndState[0]
        stateName = stateMachineAndState[1]
        r = random.random()
        if r < 0.1:
            stateMachineName = method_name_1()
        if r < 0.05 or r > 0.95:
            stateName = method_name_1()
        if 0.85 < r <= 0.95:
            stateName = random.sample(stateMachineState, 1)[0][1]
        if stateName is None:
            stateName = method_name_1()
    return "SUBSEQUENT_STATE_COUNT " + stateMachineName + " " + stateName


def stateCount1(s1):
    return "STATE_COUNT " + s1


def stateCriticalPoint1(name1, name2):
    return "STATE_IS_CRITICAL_POINT " + name1 + " " + name2


def trasitionTrigger1(s1, s2, s3):
    return "TRANSITION_TRIGGER " + s1 + " " + s2 + " " + s3


def state_critical_point():
    global stateMachineState
    if len(stateMachineState) == 0:
        stateMachineName = method_name_1()
        stateName = method_name_1()
    else:
        stateMachineAndState = random.sample(stateMachineState, 1)[0]
        stateMachineName = stateMachineAndState[0]
        stateName = stateMachineAndState[1]
        r = random.random()
        if r < 0.1:
            stateMachineName = method_name_1()
        if r < 0.05 or r > 0.95:
            stateName = method_name_1()
        if 0.85 < r <= 0.95:
            stateName = random.sample(stateMachineState, 1)[0][1]
        if stateName is None:
            stateName = method_name_1()
    return "STATE_IS_CRITICAL_POINT " + stateMachineName + " " + stateName


def trasition_trigger():
    global stateMachineState
    if len(stateMachineState) == 0:
        stateMachineName = method_name_1()
        stateName1 = method_name_1()
        stateName2 = method_name_1()
    else:
        stateMachineAndState = random.sample(stateMachineState, 1)[0]
        stateMachineName = stateMachineAndState[0]
        stateName1 = stateMachineAndState[1]
        stateName2 = stateMachineAndState[1]
        r = random.random()
        if r < 0.1:
            stateMachineName = method_name_1()
        if r < 0.05 or r > 0.95:
            stateName1 = method_name_1()
        if 0.85 < r <= 0.95:
            stateName1 = random.sample(stateMachineState, 1)[0][1]

        r = random.random()
        if r < 0.05 or r > 0.95:
            stateName2 = method_name_1()
        if 0.85 < r <= 0.95:
            stateName2 = random.sample(stateMachineState, 1)[0][1]

        if stateName1 is None:
            stateName1 = method_name_1()
        if stateName2 is None:
            stateName2 = method_name_1()
    return "TRANSITION_TRIGGER " + stateMachineName + " " + stateName1 + " " + stateName2


'''instrDic = {1: classCount, 2: classOperationCount, 3: classAttrCount, 4: classAssoCount, \
            5: classAssoClassList, 6: classOperationVisibility, 7: classAttrVisibility, \
            8: classTopBase, 9: classImplementInterfaceList, 10: classInfoHidden, 11: ptcpObjCount, \
            12: messageCount, 13: incomingMsgCount, 14: stateCount, 15: transitionCount, 16: subsequentStateCount}'''
instrDic = {1: stateCount, 2: state_critical_point, 3: trasition_trigger,
            4: ptcpObjCount, 5: ptcp_creator, 6: ptcp_lost_and_found}


def test2Stdin():
    global stdin
    for i in range(300):
        r = random.random()
        n = random.randint(1, 6)
        # n = random.sample([4,5,9],1)[0]
        # n = 2
        # n = 10
        ret = instrDic[n]()
        stdin.write(ret + '\n')


def test(src_list: list, num: int, time_limit: float) -> None:
    print('----- TEST CASE ' + str(num) + ' BEGIN -----')
    print('           TIME')
    for src in src_list:
        os.environ["COMSPEC"] = 'powershell'
        p = subprocess.Popen(
            'Measure-Command{Get-Content in.txt | java -jar ' + src + '.jar > ' + src + '.txt}',
            shell=True, stdout=subprocess.PIPE)
        p.wait()
        time_list = p.stdout.read()
        sorted_list = time_list.decode('utf-8').strip().split('\n')
        time_used = float(sorted_list[9].split(": ")[1])
        print(src + ' used : ' + str(time_used) + 's')
        if time_used >= time_limit:
            print(src + ' is TLE')
            os.system('pause')
    print('          RESULT')
    pre = src_list[0]
    for i in range(1, len(src_list)):
        os.system('fc ' + pre + '.txt ' + src_list[i] + '.txt /n > result.txt')
        result = open('result.txt', 'r')
        result.readline()
        if 'FC' not in result.readline():
            print(src_list[i] + ' is different with ' + pre)
            os.system('pause')
        pre = src_list[i]
    print('  All answers are identical')
    print('------ TEST CASE ' + str(num) + ' END ------\n\n')


if __name__ == '__main__':
    case = 1
    # while (True):
    ids = []
    classIds = []
    interfaceIds = []
    attrIds = []
    names = []
    classNames = []
    interfaceNames = []
    attrNames = []
    opNames = []
    interactionNames = []
    collaborationNames = []
    lifelineNames = []
    stateMachineNames = []
    datas = []
    asso = []
    classOp = []
    classAttr = []
    interactionLifeline = []
    stateMachineState = []
    endponitId = []
    smSt = {}
    intlife = {}
    main()
    data2Stdin()
    os.chdir('Z:\\buaa_oo_2022\\Unit4\\HW2\\hw2_test')  # change the path
    jar_list = ['homework3']  # add your Java archive's name without suffix
    test(jar_list, case, 100.0)  # you can change the third parameter to change the procedure's execute time
    case += 1
