import { useEffect, useState } from "react";
import { Modal, Pressable, StyleSheet, View, Text, Dimensions } from "react-native";
import { db } from "../firebaseConfig";
import { ScrollView } from "react-native-gesture-handler";

const deviceWidth = Dimensions.get('screen').width;
const deviceHeight = Dimensions.get('screen').height;

export default function LikeModal({modalVisible ,handleModalVisible}) {

    useEffect(()=> {
        db.ref('/like').once('value',(snapshot) => {
            console.log(snapshot)
        })
    },[])

    return(
        <Modal style={modalStyle.modal} animationType="slide" transparent={true} visible={modalVisible}>
            <View style={modalStyle.modalContainer}>
                <Text style={modalStyle.modalHeader}>내 찜 리스트</Text>

                <ScrollView>
                    <View>

                    </View>
                </ScrollView>
                {/* 부모 컴포넌트 state 호출하기 : setModalVisible(false)로 */}
                <Pressable style={modalStyle.closeButton} onPress={()=> handleModalVisible()} >
                    <Text style={modalStyle.closeText}>닫기</Text>
                </Pressable>
            </View>
        </Modal>
    )
}

const modalStyle = StyleSheet.create({
    modal: {
        flex:1,
        justifyContent: 'center',
        alignItems: 'center',
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
    },
    closeButton: {
        width: (deviceWidth*0.88) - 50,
        height: 40,
        backgroundColor: '#00a86b',
        shadowColor: '#00a86b',
        shadowOffset: {
            x:2,
            y:2
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