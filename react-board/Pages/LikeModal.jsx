import { useEffect, useState } from "react";
import { Modal, Pressable, StyleSheet, View, Text, Dimensions } from "react-native";
import { dbRef } from "../firebaseConfig";
import { get, child } from "firebase/database";
import { ScrollView } from "react-native-gesture-handler";

const deviceWidth = Dimensions.get('screen').width;
const deviceHeight = Dimensions.get('screen').height;

export default function LikeModal({modalVisible ,handleModalVisible}) {
    const [title, setTitles] = useState([])

    useEffect(()=> {
        if(modalVisible) {
            get(child(dbRef, 'like/')).then((snapshot) => {
                const temp = []
                snapshot.forEach(item => {
                    temp.push(item.val().title)
            })
            setTitles(temp) 
        })}
    }, [modalVisible])

    return(
        <Modal animationType="slide" transparent={true} visible={modalVisible}>
            <View style={modalStyle.modal}>
                <View style={modalStyle.modalContainer}>
                    <Text style={modalStyle.modalHeader}>내 찜 리스트</Text>

                    <ScrollView>
                        { 
                        title.map((v,i)=> {
                            return (
                                <View style={modalStyle.modalItemContainer} key={i}>
                                    <Text>{v}</Text>
                                </View> 
                            )})
                        }
                    </ScrollView>
                    {/* 부모 컴포넌트 state 호출하기 : setModalVisible(false)로 */}
                    <Pressable style={modalStyle.closeButton} onPress={()=> handleModalVisible()} >
                        <Text style={modalStyle.closeText}>닫기</Text>
                    </Pressable>
                </View>
            </View>
        </Modal>
    )
}

const modalStyle = StyleSheet.create({
    modal: {
        flex:1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(255, 255, 255, 0.13)'
    },
    modalContainer: {
        padding: 10,
        width: deviceWidth*0.88,
        height: deviceHeight*0.5,
        borderRadius: 10,
        // justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
        shadowOpacity: 0.3,
        shadowOffset: {
          x: 1,
          y: 5
        },
        shadowColor: '#090909'
    },
    modalHeader: {
        fontSize: 20,
        fontWeight: 800,
        marginVertical: 12
    }, 
    modalItemContainer : {
        marginBottom: 7,
        width: (deviceWidth*0.88) - 50,
        height: 45,
        borderRadius: 20,
        backgroundColor: 'rgb(246, 246, 246)',
        justifyContent: 'center',
        alignItems: 'left',
        paddingLeft: 20,
        shadowColor: '#d3d3d3',
        shadowOffset: {
            x:1,
            y:1
        },
        shadowOpacity: 0.1,
    }, 
    closeButton: {
        width: (deviceWidth*0.88) - 50,
        height: 40,
        backgroundColor: '#00a86b',
        shadowColor: '#d3d3d3',
        shadowOffset: {
            x:1,
            y:1
        },
        shadowOpacity: 0.2,
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 40,
    },
    closeText: {
        color: '#fff',
        fontWeight: 500,
    }
  })